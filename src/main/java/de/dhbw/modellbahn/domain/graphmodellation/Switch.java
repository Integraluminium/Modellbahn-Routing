package de.dhbw.modellbahn.domain.graphmodellation;

public interface Switch {
    void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException;
}
