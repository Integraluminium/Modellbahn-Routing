package de.dhbw.modellbahn.application.routing.directed.graph;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;

import java.util.Objects;

public record WeightedDistanceEdge(GraphPoint point, Distance distance) {
    public WeightedDistanceEdge {
        Objects.requireNonNull(point, "Point must not be null.");
        Objects.requireNonNull(distance, "Distance must not be null.");
    }
}
