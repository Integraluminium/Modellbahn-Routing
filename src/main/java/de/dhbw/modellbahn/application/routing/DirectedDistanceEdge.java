package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;

import java.util.Objects;

public record DirectedDistanceEdge(DirectedNode node, Distance distance) {
    public DirectedDistanceEdge {
        Objects.requireNonNull(node, "Node must not be null.");
        Objects.requireNonNull(distance, "Distance must not be null.");
    }
}
