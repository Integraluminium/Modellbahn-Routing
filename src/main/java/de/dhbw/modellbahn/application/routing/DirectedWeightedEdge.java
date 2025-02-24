package de.dhbw.modellbahn.application.routing;

import java.util.Objects;

public record DirectedWeightedEdge(DirectedNode node, Weight weight) {
    public DirectedWeightedEdge {
        Objects.requireNonNull(node, "Node must not be null.");
        Objects.requireNonNull(weight, "Weight must not be null.");
    }
}
