package de.dhbw.modellbahn.adapter.moba.communication;

public record TrackComponentStatus(int status) {
    public TrackComponentStatus {
        if (status < 0 || status > 1) {
            throw new IllegalArgumentException("status must be 0 or 1");
        }
    }
}
