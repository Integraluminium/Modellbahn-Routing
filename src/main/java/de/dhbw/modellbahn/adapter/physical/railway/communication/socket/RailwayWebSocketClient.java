package de.dhbw.modellbahn.adapter.physical.railway.communication.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.S88EventCommand;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RailwayWebSocketClient implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(RailwayWebSocketClient.class.getSimpleName());
    private final String websocketUrl;
    private final ObjectMapper objectMapper;
    private final Map<Integer, Consumer<Integer>> contactListeners = new ConcurrentHashMap<>();
    private final Map<String, MessageHandler<?>> messageHandlers = new ConcurrentHashMap<>();
    private WebSocket webSocket;
    private boolean connected = false;

    public RailwayWebSocketClient(String host, int port) {
        this.websocketUrl = "ws://" + host + ":" + port;
        this.objectMapper = new ObjectMapper();

        registerMessageHandler("S88EventCommand", S88EventCommand.class, this::handleS88Event);
    }

    public CompletableFuture<Void> connect() {
        if (isConnected()) {
            logger.warning("Already connected to WebSocket");
            return CompletableFuture.completedFuture(null);
        }

        logger.info("Connecting to websocket at: " + websocketUrl);
        return HttpClient.newHttpClient()
                .newWebSocketBuilder()
                .buildAsync(URI.create(websocketUrl), new WebSocketListener(this))
                .thenAccept(ws -> {
                    this.webSocket = ws;
                    logger.info("WebSocket connection established");
                })
                .exceptionally(ex -> {
                    logger.severe("WebSocket connection failed: " + ex.getMessage());
                    connected = false;
                    return null;
                });
    }

    public void registerContactListener(int contactId, Consumer<Integer> callback) {
        contactListeners.put(contactId, callback);
    }

    public void unregisterContactListener(int contactId) {
        contactListeners.remove(contactId);
    }

    @Override
    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Closing connection");
        }
    }

    /**
     * Registers a message handler for a specific command type
     */
    public <T> void registerMessageHandler(String commandPrefix, Class<T> type, Consumer<T> handler) {
        messageHandlers.put(commandPrefix, new MessageHandler<>(type, handler));
        logger.fine("Registered handler for command type: " + commandPrefix);
    }

    /**
     * Notifies a registered contact listener
     */
    private void notifyContactListener(Integer contactId) {
        Consumer<Integer> listener = contactListeners.get(contactId);
        if (listener != null) {
            try {
                listener.accept(contactId);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error in contact listener for ID " + contactId, e);
            }
        }
    }


    /**
     * Handles S88Event messages
     */
    private void handleS88Event(S88EventCommand event) {
        if (event.getContactId() == null) {
            logger.warning("Received S88 event with null contact ID");
            return;
        }

        Integer contactId = event.getContactId();

        // Contact activation (0->1)
        if (event.getStateOld() != null && event.getStateOld() == 0 &&
                event.getStateNew() != null && event.getStateNew() == 1) {
            logger.info("Track contact activated: " + contactId);
            notifyContactListener(contactId);
        }
        // Contact deactivation (1->0)
        else if (event.getStateOld() != null && event.getStateOld() == 1 &&
                event.getStateNew() != null && event.getStateNew() == 0) {
            logger.info("Track contact deactivated: " + contactId);
            notifyContactListener(contactId);
        }
    }


    Map<String, MessageHandler<?>> getMessageHandlers() {
        return Collections.unmodifiableMap(messageHandlers);
    }


    ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public boolean isConnected() {
        return connected;
    }
}