package de.dhbw.modellbahn.plugin.logging;

import java.io.OutputStream;
import java.util.logging.*;

public class MobaLogConfig {


    public static void configureLogging(Level level) {
        OutputStream outputStream = System.out;
        configureLogging(level, outputStream);
    }

    public static void configureLogging(Level level, OutputStream outputStream) {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);

        // Remove default handlers
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        Handler handler = new StreamHandler(outputStream, createSimpleFormatter());
        handler.setLevel(level);

        rootLogger.addHandler(handler);
    }

    private static SimpleFormatter createSimpleFormatter() {
        return new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("[%s] %s: %s%n", record.getLevel(), record.getLoggerName(), record.getMessage());
            }
        };
    }
}