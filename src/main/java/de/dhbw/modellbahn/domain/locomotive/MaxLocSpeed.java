package de.dhbw.modellbahn.domain.locomotive;

public record MaxLocSpeed(double value) {
    public MaxLocSpeed {
        if (value <= 0) {
            throw new IllegalArgumentException("Value must be positive.");
        }
    }
}
