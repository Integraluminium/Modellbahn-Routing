package de.dhbw.modellbahn.domain.graph;

public record GraphPointConnection(GraphPoint point1, GraphPoint point2) {

    public boolean connects(GraphPoint p1, GraphPoint p2) {
        return p1 == point1 && p2 == point2 || p1 == point2 && p2 == point1;
    }
}
