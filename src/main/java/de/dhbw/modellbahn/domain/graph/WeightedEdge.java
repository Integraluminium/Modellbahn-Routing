package de.dhbw.modellbahn.domain.graph;

public record WeightedEdge(GraphPoint destination, Distance distance, Height height, boolean electrified) {
    public WeightedEdge {
        if (destination == null || distance == null || height == null){
            throw new IllegalArgumentException("Destination, distance and height must not be null.");
        }
    }
}
