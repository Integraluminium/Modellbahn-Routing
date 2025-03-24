package de.dhbw.modellbahn.domain.locomotive.attributes.resources;

public enum FuelType {
    FUEL_A(1),
    FUEL_B(2),
    SAND(3);
    private final int type;

    FuelType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
