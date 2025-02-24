package de.dhbw.modellbahn.application.routing;

public record Weight(double value) {
    public Weight {
        if (value < 0) {
            throw new IllegalArgumentException("Weight must have a positive value.");
        }
    }

    public Weight add(double value) {
        return new Weight(this.value() + value);
    }

    public Weight add(Weight weight) {
        return add(weight.value());
    }
}
