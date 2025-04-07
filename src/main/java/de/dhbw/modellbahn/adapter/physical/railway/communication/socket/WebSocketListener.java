package de.dhbw.modellbahn.adapter.physical.railway.communication.socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.http.WebSocket;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

/**
 * WebSocket listener to handle incoming messages
 */
public class WebSocketListener implements WebSocket.Listener {
    private static final Logger logger = Logger.getLogger(WebSocketListener.class.getSimpleName());
    private final StringBuilder messageBuilder = new StringBuilder();
    private final RailwayWebSocketClient railwayWebSocketClient;

    public WebSocketListener(final RailwayWebSocketClient railwayWebSocketClient) {
        this.railwayWebSocketClient = railwayWebSocketClient;
    }

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

    private void processMessage(String message) {
        // Find the appropriate handler for this message type
        Map<String, MessageHandler<?>> messageHandlers = railwayWebSocketClient.getMessageHandlers();
        ObjectMapper objectMapper = railwayWebSocketClient.getObjectMapper();

        for (Map.Entry<String, MessageHandler<?>> entry : messageHandlers.entrySet()) {
            if (message.startsWith(entry.getKey())) {
                // Extract JSON and process with the handler
                int jsonStart = message.indexOf('{');
                if (jsonStart >= 0) {
                    String jsonStr = message.substring(jsonStart);
                    entry.getValue().handleMessage(jsonStr, objectMapper);
                    return;
                }
            }
        }

        // Log if no handler found
        logger.finer("No handler found for message: " +
                (message.length() > 100 ? message.substring(0, 100) + "..." : message));
    }
}
