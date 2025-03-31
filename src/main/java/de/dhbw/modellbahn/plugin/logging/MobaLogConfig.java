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

        Handler handler = createHandler(outputStream);
        handler.setLevel(level);

        rootLogger.addHandler(handler);
    }

    private static Handler createHandler(final OutputStream outputStream) {
        return new StreamHandler(outputStream, createSimpleFormatter()) {
            @Override
            public synchronized void publish(final LogRecord record) {
                super.publish(record);
                flush();
            }
        };
    }

    private static Formatter createSimpleFormatter() {
        return new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("[%s] %s: %s%n", record.getLevel(), record.getLoggerName(), record.getMessage());
            }
        };
    }
}