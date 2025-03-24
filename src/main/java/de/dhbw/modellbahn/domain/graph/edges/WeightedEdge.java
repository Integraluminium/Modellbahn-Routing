package de.dhbw.modellbahn.domain.graph.edges;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Height;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;

public record WeightedEdge(GraphPoint destination, Distance distance, Height height, boolean electrified) {
    public WeightedEdge {
        if (destination == null || distance == null || height == null){
            throw new IllegalArgumentException("Destination, distance and height must not be null.");
        }
    }
}
