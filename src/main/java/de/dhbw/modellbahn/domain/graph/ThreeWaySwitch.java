package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

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

    private void switchStraight() {
        firstSwitch.setStraight();
        secondSwitch.setStraight();
    }

    private void switchLeft() {
        firstSwitch.setDiverging();
    }

    private void switchRight() {
        firstSwitch.setStraight();
        secondSwitch.setDiverging();
    }

    public boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return connectsStraight(point1, point2) || connectsLeft(point1, point2) || connectsRight(point1, point2);
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

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        if (point.equals(root)) {
            return SwitchSide.IN;
        } else if (point.equals(straight) || point.equals(left) || point.equals(right)) {
            return SwitchSide.OUT;
        } else {
            return SwitchSide.UNDEFINED;
        }
    }
}
