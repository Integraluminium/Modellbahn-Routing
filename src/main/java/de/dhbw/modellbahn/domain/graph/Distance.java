package de.dhbw.modellbahn.domain.graph;

public record Distance(int value) {
    public Distance {
        if (value < 0) {
            throw new IllegalArgumentException("Distance value must be positive or 0.");
        }
    }

    public Distance add(int value) {
        return new Distance(this.value() + value);
    }

    public Distance add(Distance distance) {
        return this.add(distance.value());
    }
}
