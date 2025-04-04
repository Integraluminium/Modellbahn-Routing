package de.dhbw.modellbahn.adapter.physical.railway.communication.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.S88EventCommand;
import de.dhbw.modellbahn.domain.events.DomainEventPublisher;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class RailwayWebSocketClient implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(RailwayWebSocketClient.class.getSimpleName());
    private final String websocketUrl;
    private final ObjectMapper objectMapper;

    private final Map<String, MessageHandler<?>> messageHandlers = new ConcurrentHashMap<>();
    private WebSocket webSocket;
    private boolean connected = false;

    public RailwayWebSocketClient(String host, int port) {
        this.websocketUrl = "ws://" + host + ":" + port;
        this.objectMapper = new ObjectMapper();
        DomainEventPublisher eventPublisher = DomainEventPublisher.getInstance();
        TrackContactHandler trackContactHandler = new TrackContactHandler(eventPublisher);

        registerMessageHandler("S88EventCommand", S88EventCommand.class, trackContactHandler::handleEvent);
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