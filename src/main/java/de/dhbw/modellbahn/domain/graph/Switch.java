package de.dhbw.modellbahn.domain.graph;

public interface Switch {
    void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException;
    void switchToConnectToPoint(GraphPoint point);

    boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2);
    SwitchSide getSwitchSideFromPoint(GraphPoint point);
}
