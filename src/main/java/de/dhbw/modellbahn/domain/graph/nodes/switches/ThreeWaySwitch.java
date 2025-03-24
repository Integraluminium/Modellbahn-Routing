package de.dhbw.modellbahn.domain.graph.nodes.switches;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointSide;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchComponent;

public class ThreeWaySwitch extends GraphPoint implements Switch {
    private final SwitchComponent firstSwitch;
    private final SwitchComponent secondSwitch;

    private final PointName root;
    private final PointName straight;
    private final PointName left;
    private final PointName right;

    public ThreeWaySwitch(PointName name, SwitchComponent firstSwitch, SwitchComponent secondSwitch, PointName root, PointName straight, PointName left, PointName right) {
        super(name);
        this.firstSwitch = firstSwitch;
        this.secondSwitch = secondSwitch;
        this.root = root;
        this.straight = straight;
        this.left = left;
        this.right = right;
    }

    public void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException {
        if (connectsRight(point1, point2)) {
            switchRight();
        } else if (connectsStraight(point1, point2)) {
            switchStraight();
        } else if (connectsLeft(point1, point2)) {
            switchLeft();
        } else {
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }

    public boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return connectsStraight(point1, point2) || connectsLeft(point1, point2) || connectsRight(point1, point2);
    }

    public PointSide getSwitchSideFromPoint(GraphPoint point) {
        if (point.equals(root)) {
            return PointSide.IN;
        } else if (point.equals(straight) || point.equals(left) || point.equals(right)) {
            return PointSide.OUT;
        } else {
            throw new IllegalArgumentException("Illegal point");
        }
    }

    public GraphPoint getPointThatCanConnectThisPoint(GraphPoint point) {
        PointName pointName = point.getName();
        if (pointName.equals(root)) {
            return new GraphPoint(left);
        } else if (pointName.equals(left) || pointName.equals(straight) || pointName.equals(right)) {
            return new GraphPoint(root);
        } else {
            throw new IllegalArgumentException("This point is not connected with the switch");
        }
    }

    private void switchStraight() {
        logger.fine(getClass().getSimpleName() + " " + getName() + " switchStraight");
        firstSwitch.setStraight();
        secondSwitch.setStraight();
    }

    private void switchLeft() {
        logger.fine(getClass().getSimpleName() + " " + getName() + " switch Left");
        firstSwitch.setDiverging();
    }

    private void switchRight() {
        logger.fine(getClass().getSimpleName() + " " + getName() + " switch Right");
        firstSwitch.setStraight();
        secondSwitch.setDiverging();
    }

    private boolean connectsStraight(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(straight)) || (point2.equals(root) && point1.equals(straight));
    }

    private boolean connectsLeft(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(left)) || (point2.equals(root) && point1.equals(left));
    }

    private boolean connectsRight(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(right)) || (point2.equals(root) && point1.equals(right));
    }
}
