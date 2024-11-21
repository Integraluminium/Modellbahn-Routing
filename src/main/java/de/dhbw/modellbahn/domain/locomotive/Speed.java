package de.dhbw.modellbahn.domain.locomotive;

public record Speed(int value) {
    public Speed {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Speed value must be in range [0, 100].");
        }
    }
}
