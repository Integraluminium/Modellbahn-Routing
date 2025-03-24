package de.dhbw.modellbahn.domain.graph.edges;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;

public record GraphConnection(
        GraphPoint startPoint,
        WeightedEdge weightedEdge
) {
}
