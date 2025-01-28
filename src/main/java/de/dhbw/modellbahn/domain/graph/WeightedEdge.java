package de.dhbw.modellbahn.domain.graph;

public record WeightedEdge(GraphPoint destination, Distance distance) {
    public WeightedEdge {
        if (destination == null || distance == null){
            throw new IllegalArgumentException("Destination and distance must not be null.");
        }
    }
}
