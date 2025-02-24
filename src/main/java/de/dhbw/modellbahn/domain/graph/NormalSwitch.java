package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class NormalSwitch extends GraphPoint implements Switch {
    private final SwitchComponent switchComponent;

    private final PointName root;
    private final PointName straight;
    private final PointName turnout;

    public NormalSwitch(PointName name, SwitchComponent switchComponent, PointName root, PointName straight, PointName turnout) {
        super(name);
        this.switchComponent = switchComponent;
        this.root = root;
        this.straight = straight;
        this.turnout = turnout;
    }

    public void switchToConnectPoints(GraphPoint point1, GraphPoint point2) {
        if (connectsStraight(point1, point2)) {
            switchComponent.setStraight();
        } else if (connectsDiverging(point1, point2)) {
            switchComponent.setDiverging();
        } else {
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }

    public boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return connectsStraight(point1, point2) || connectsDiverging(point1, point2);
    }

    public PointSide getSwitchSideFromPoint(GraphPoint point) {
        if (point.equals(root)) {
            return PointSide.IN;
        } else if (point.equals(straight) || point.equals(turnout)) {
            return PointSide.OUT;
        } else {
            throw new IllegalArgumentException("Point is not connected to this switch.");
        }
    }

    private boolean connectsStraight(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(straight))
                || (point2.equals(root) && point1.equals(straight));
    }

    private boolean connectsDiverging(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(turnout))
                || (point2.equals(root) && point1.equals(turnout));
    }

    public GraphPoint getPointThatCanConnectThisPoint(GraphPoint point) {
        PointName pointName = point.getName();
        if (pointName.equals(root)) {
            return new GraphPoint(straight);
        } else if (pointName.equals(straight) || pointName.equals(turnout)) {
            return new GraphPoint(root);
        } else {
            throw new IllegalArgumentException("This point is not connected with the switch");
        }
    }
}
