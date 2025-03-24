package de.dhbw.modellbahn.util;

import java.util.logging.*;

public class MobaLogConfig {
    public static void configureLogging(Level level) {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);

        // Remove default handlers
        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        // Add custom handler with better formatting
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(level);
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                return String.format("[%s] %s: %s%n",
                        record.getLevel(),
                        record.getLoggerName(),
                        record.getMessage());
            }
        });
        rootLogger.addHandler(handler);
    }
}