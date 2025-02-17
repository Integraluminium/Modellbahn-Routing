package de.dhbw.modellbahn.domain.graph;

import java.util.Objects;

public class GraphPoint {
    private final PointName name;

    public GraphPoint(PointName name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be an empty string or null.");
        }
        this.name = name;
    }

    public PointName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GraphPoint that = (GraphPoint) o;
        return this.name.equals(that.getName());
    }

    public boolean equals(PointName s) {
        return this.name.equals(s);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }
}
