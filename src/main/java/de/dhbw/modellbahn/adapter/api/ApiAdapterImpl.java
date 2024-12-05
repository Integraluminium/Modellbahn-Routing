package de.dhbw.modellbahn.adapter.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.dhbw.modellbahn.adapter.api.dto.SystemGoCommand;
import de.dhbw.modellbahn.adapter.api.dto.SystemStopCommand;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class ApiAdapterImpl implements ApiAdapter {
    private final String url;
    private final int senderHash;


    public ApiAdapterImpl(int senderHash, String url) {
        this.senderHash = senderHash;
        this.url = url != null ? url : "http://127.0.0.1:8002";
        //  this.gson = new Gson();
    }

    private static <T> String transformDtoToJson(T bodyObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bodyObject);
    }

    private static void sendHttpRequest(URI uri, String jsonBody) throws IOException, InterruptedException {
        try (HttpClient client = HttpClient.newHttpClient()) {
            // Build the HttpRequest with the JSON body
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json") // Set the content type to application/json
                    .timeout(Duration.ofSeconds(10)) // Set a timeout for the request
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

    public <T> void sendRequest(String locator, T bodyObject) {
        try {
            String jsonBody = transformDtoToJson(bodyObject);

            System.out.printf(jsonBody);

//            URI uri = URI.create(url + locator);
//            sendHttpRequest(uri, jsonBody);

        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getSenderHash() {
        return senderHash;
    }


    @Override
    public void systemStop() {
        sendRequest(url + "system/stop", new SystemStopCommand(
                senderHash,
                true,
                0
        ));
    }

    @Override
    public void systemGo() {
        sendRequest(url + "system/go", new SystemGoCommand(
                senderHash,
                true,
                0
        ));

    }
}
