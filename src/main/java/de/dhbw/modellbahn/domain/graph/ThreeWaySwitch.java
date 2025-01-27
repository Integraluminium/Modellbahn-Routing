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

    @Override
    public void switchToConnectPoints(GraphPoint point1, GraphPoint point2) throws IllegalArgumentException {
        if (right.connects(point1, point2)) {
            firstSwitch.setStraight();
            secondSwitch.setDiverging();
        } else if (straight.connects(point1, point2)) {
            firstSwitch.setStraight();
            secondSwitch.setStraight();
        } else if (left.connects(point1, point2)) {
            firstSwitch.setDiverging();
        } else {
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }

    public void switchToConnectToPoint(GraphPoint point) {

    }

    public boolean checkIfSwitchConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return straight.connects(point1, point2) || left.connects(point1, point2) || right.connects(point1, point2);
    }

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        return null;
    }
}
