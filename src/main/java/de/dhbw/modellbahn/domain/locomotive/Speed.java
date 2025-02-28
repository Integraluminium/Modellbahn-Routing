package de.dhbw.modellbahn.domain.locomotive;

public record Speed(int value) {
    public Speed {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Speed value must be in range [0, 100].");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Speed that = (Speed) o;
        return this.value() == that.value();
    }

    public boolean equals(int value){
        return this.value() == value;
    }

    @Override
    public int hashCode() {
        return value();
    }
}
