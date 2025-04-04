package de.dhbw.modellbahn.adapter.physical.railway.communication.socket;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generic message handler for different command types
 */
record MessageHandler<T>(Class<T> type, Consumer<T> handler) {

    public void handleMessage(String jsonStr, ObjectMapper mapper) {
        try {
            T obj = mapper.readValue(jsonStr, type);
            handler.accept(obj);
        } catch (Exception e) {
            Logger.getLogger(MessageHandler.class.getSimpleName())
                    .log(Level.WARNING, "Error parsing message as " + type.getSimpleName(), e);
        }
    }
}