package de.dhbw.modellbahn.domain.locomotive.ressources;

/**
 * Value for Ressources
 *
 * @param value - max 255
 */
public record FuelValue(short value) {
    public FuelValue {
        if (value < 0) {
            throw new IllegalArgumentException("Fuel value cannot be negative");
        }
    }
}
