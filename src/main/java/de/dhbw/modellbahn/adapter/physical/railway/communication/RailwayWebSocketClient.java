package de.dhbw.modellbahn.adapter.physical.railway.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.S88EventCommand;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class RailwayWebSocketClient {
    private static final Logger logger = Logger.getLogger(RailwayWebSocketClient.class.getSimpleName());
    private final String websocketUrl;
    private final ObjectMapper objectMapper;
    private final Map<Integer, Consumer<Integer>> contactListeners = new ConcurrentHashMap<>();
    private WebSocket webSocket;

    public RailwayWebSocketClient(String host, int port) {
        this.websocketUrl = "ws://" + host + ":" + port;
        this.objectMapper = new ObjectMapper();
    }

    public CompletableFuture<Void> connect() {
        logger.info("Connecting to websocket at: " + websocketUrl);
        return HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(websocketUrl), new WebSocketListener())
                .thenAccept(ws -> {
                    this.webSocket = ws;
                    logger.info("WebSocket connection established");
                });
    }

    public void registerContactListener(int contactId, Consumer<Integer> callback) {
        contactListeners.put(contactId, callback);
    }

    public void unregisterContactListener(int contactId) {
        contactListeners.remove(contactId);
    }

    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Closing connection");
        }
    }

    private class WebSocketListener implements WebSocket.Listener {
        private final StringBuilder messageBuilder = new StringBuilder();

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            messageBuilder.append(data);

            if (last) {
                String message = messageBuilder.toString();
                messageBuilder.setLength(0);

                try {
                    processMessage(message);
                } catch (Exception e) {
                    logger.warning("Error processing message: " + e.getMessage());
                }
            }
            webSocket.request(1);
            return null;
        }

        private void processMessage(String message) throws Exception {
            if (message.startsWith("S88EventCommand")) {
                int jsonStart = message.indexOf('{');
                if (jsonStart < 0) return;

                String jsonStr = message.substring(jsonStart);
                S88EventCommand event = objectMapper.readValue(jsonStr, S88EventCommand.class);

                // Check if it's a contact activation (0->1)
                if (event.getStateOld() != null && event.getStateOld() == 0 &&
                        event.getStateNew() != null && event.getStateNew() == 1) {

                    Integer contactId = event.getContactId();
                    logger.info("Track contact activated: " + contactId);

                    // Notify listener if registered
                    Consumer<Integer> listener = contactListeners.get(contactId);
                    if (listener != null) {
                        listener.accept(contactId);
                    }
                }
            }
        }
    }
}