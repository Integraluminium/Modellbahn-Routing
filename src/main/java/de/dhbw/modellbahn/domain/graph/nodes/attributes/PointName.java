package de.dhbw.modellbahn.domain.graph.nodes.attributes;

import java.util.Objects;

public record PointName(String name) {

    public PointName {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name must not be null or empty string.");
        }
    }

    public boolean equals(String name) {
        return this.name.equals(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PointName pointName = (PointName) o;
        return Objects.equals(name, pointName.name());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
