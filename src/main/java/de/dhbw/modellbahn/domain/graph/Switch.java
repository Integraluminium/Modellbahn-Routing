package de.dhbw.modellbahn.domain.graph;

public interface Switch {
    void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException;

    boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2);

    PointSide getSwitchSideFromPoint(GraphPoint point);

    GraphPoint getPointThatCanConnectThisPoint(GraphPoint point);
}
