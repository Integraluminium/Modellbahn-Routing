package de.dhbw.modellbahn.domain.graph;

public record Distance(int value) {
    public Distance {
        if (value < 0) {
            throw new IllegalArgumentException("Distance value must be positive.");
        }
    }
}
