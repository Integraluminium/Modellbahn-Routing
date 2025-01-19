package de.dhbw.modellbahn.domain.graph;

import java.util.Objects;

public abstract class GraphPoint {
    private final String name;

    protected GraphPoint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GraphPoint that = (GraphPoint) o;
        return Objects.equals(this.name, that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }
}
