package de.dhbw.modellbahn.domain.graph;

public record GraphConnection(
        GraphPoint startPoint,
        WeightedEdge weightedEdge
) {
}
