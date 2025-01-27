package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class ThreeWaySwitch extends GraphPoint implements Switch {
    private final GraphPointConnection straight;
    private final GraphPointConnection left;
    private final GraphPointConnection right;

    private final SwitchComponent firstSwitch;
    private final SwitchComponent secondSwitch;

    public ThreeWaySwitch(String name, GraphPointConnection straight, GraphPointConnection left, GraphPointConnection right, SwitchComponent firstSwitch, SwitchComponent secondSwitch) {
        super(name);
        this.straight = straight;
        this.left = left;
        this.right = right;
        this.firstSwitch = firstSwitch;
        this.secondSwitch = secondSwitch;
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
