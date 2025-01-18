package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class ThreeWaySwitch extends GraphPoint implements Switch {
    private final GraphPointConnection straight;
    private final GraphPointConnection left;
    private final GraphPointConnection right;

    private final SwitchComponent firstSwitch;
    private final SwitchComponent secondSwitch;

    public ThreeWaySwitch(GraphPointConnection straight, GraphPointConnection left, GraphPointConnection right, SwitchComponent firstSwitch, SwitchComponent secondSwitch) {
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
}
