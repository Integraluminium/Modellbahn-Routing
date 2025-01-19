package de.dhbw.modellbahn.domain.graph;

public interface Switch {
    void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException;

    boolean checkIfSwitchConnectsPoints(GraphPoint point1, GraphPoint point2);
}
