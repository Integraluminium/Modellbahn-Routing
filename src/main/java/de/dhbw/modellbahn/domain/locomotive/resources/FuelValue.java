package de.dhbw.modellbahn.domain.locomotive.resources;

/**
 * Value for Ressources
 *
 * @param value - max 255
 */
public record FuelValue(int value) {
    public FuelValue {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException("Fuel must be in range [0;255]");
        }
    }
}
