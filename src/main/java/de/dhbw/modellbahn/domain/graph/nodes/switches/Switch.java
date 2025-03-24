package de.dhbw.modellbahn.domain.graph.nodes.switches;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;

public interface Switch {
    void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException;

    boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2);

    /**
     * Determines to which side of the switch the graph point is connected to.
     * <p><toModifySystem>Warning</toModifySystem> Result is not inversed
     * <p>Example: If the point is connected to the root, it will return {@link PointSide#IN}
     * If the point is connected to the outgoing point, it will return {@link PointSide#OUT}
     *
     * @param point The point to check
     * @return The side of the switch the point is connected to
     */
    PointSide getSwitchSideFromPoint(GraphPoint point);

    GraphPoint getPointThatCanConnectThisPoint(GraphPoint point);
}
