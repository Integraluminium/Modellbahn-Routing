package de.dhbw.modellbahn.adapter.moba.config;

public class ApiConfig {
    private final int requestTimeout;

    public ApiConfig(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }
}
