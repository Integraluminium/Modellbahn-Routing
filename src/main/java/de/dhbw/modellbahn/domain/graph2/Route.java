package de.dhbw.modellbahn.domain.graph2;

import java.util.Objects;

public record Route(DirectedNode start, DirectedNode end, double weight) {
    public Route {
        Objects.requireNonNull(start, "Start node must not be null");
        Objects.requireNonNull(end, "End node must not be null");
    }

    @Override
    public String toString() {
        return "Route " +
                start.getNodeName() +
                " -> " +
                end.getNodeName();
    }
}
