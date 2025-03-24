package de.dhbw.modellbahn.domain.graph.nodes.nonswitches;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;

import java.util.Objects;
import java.util.logging.Logger;

public class GraphPoint {
    protected static final Logger logger = Logger.getLogger(GraphPoint.class.getSimpleName());
    private final PointName name;

    public GraphPoint(PointName name) {
        if (name == null) {
            throw new IllegalArgumentException("Name must not be an empty string or null.");
        }
        this.name = name;
    }

    public static GraphPoint of(String name) {
        return new GraphPoint(new PointName(name));
    }

    public PointName getName() {
        return name;
    }

    public boolean equals(PointName s) {
        return this.name.equals(s);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.name);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof GraphPoint that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public String toString() {
        return String.format("GP-%s{%s}", getClass().getSimpleName(), name.name());
    }
}
