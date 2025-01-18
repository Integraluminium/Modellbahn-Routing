package de.dhbw.modellbahn.adapter.moba.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Objects;

public class ApiService {
    private final String url;
    private final int senderHash;
    private Duration timeout;


    public ApiService(int senderHash, String url, int timeout) {
        this.url = Objects.requireNonNull(url, "URL must not be be null");
        if (senderHash < 0) {
            throw new IllegalArgumentException("Sender hash must be positive");
        }
        this.senderHash = senderHash;
        setTimeout(timeout);
    }

    public ApiService(int senderHash) {
        this(senderHash, "http://127.0.0.1:8002", 10);
    }

    private static <T> String transformDtoToJson(T bodyObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bodyObject);
    }

    private static void sendHttpRequest(URI uri, String jsonBody, Duration timeout) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            // Build the HttpRequest with the JSON body
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json") // Set the content type to application/json
                    .timeout(timeout) // Set a timeout for the request
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody)) // Set the body
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the response status code
            if (response.statusCode() != 200) {
                System.err.println("Request failed with status code: " + response.statusCode());
            }
        } catch (HttpTimeoutException e) {
            System.err.println("Request timed out: " + e.getMessage());
        }
    }

    public void setTimeout(final int timeout) {
        if (timeout < 1) {
            throw new IllegalArgumentException("Timeout must be positive");
        }
        this.timeout = Duration.ofSeconds(timeout);
    }

    public <T> void sendRequest(String locator, T bodyObject) {
        try {
            String jsonBody = transformDtoToJson(bodyObject);

            System.out.printf(jsonBody);

            URI uri = URI.create(url + locator);
            sendHttpRequest(uri, jsonBody, this.timeout);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getSenderHash() {
        return senderHash;
    }

}
