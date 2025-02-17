package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class ThreeWaySwitch extends GraphPoint implements Switch {
    private final SwitchComponent firstSwitch;
    private final SwitchComponent secondSwitch;

    private final GraphPoint root;
    private final GraphPoint straight;
    private final GraphPoint left;
    private final GraphPoint right;

    public ThreeWaySwitch(String name, SwitchComponent firstSwitch, SwitchComponent secondSwitch, GraphPoint root, GraphPoint straight, GraphPoint left, GraphPoint right) {
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

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        if (point == root) {
            return SwitchSide.IN;
        } else if (point == straight || point == left || point == right) {
            return SwitchSide.OUT;
        } else {
            throw new IllegalArgumentException("Point is not connected to this switch.");
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

    private boolean connectsStraight(GraphPoint point1, GraphPoint point2) {
        return (point1 == root && point2 == straight) || (point2 == root && point1 == straight);
    }

    private boolean connectsLeft(GraphPoint point1, GraphPoint point2) {
        return (point1 == root && point2 == left) || (point2 == root && point1 == left);
    }

    private boolean connectsRight(GraphPoint point1, GraphPoint point2) {
        return (point1 == root && point2 == right) || (point2 == root && point1 == right);
    }
}
