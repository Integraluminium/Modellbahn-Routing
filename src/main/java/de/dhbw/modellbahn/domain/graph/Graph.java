package de.dhbw.modellbahn.domain.graph;

import java.util.List;

public class Graph {
    private final List<GraphPoint> graphPoints;
    private final List<GraphPointConnection> graphPointConnections;

    public Graph(List<GraphPoint> graphPoints, List<GraphPointConnection> graphPointConnections) {
        this.graphPoints = graphPoints;
        this.graphPointConnections = graphPointConnections;
    }
}
