package de.dhbw.modellbahn.domain.locomotive;

import java.util.Objects;

public record LocName(String name) {

    public LocName {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name must not be null or empty string.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocName locName = (LocName) o;
        return Objects.equals(name, locName.name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
