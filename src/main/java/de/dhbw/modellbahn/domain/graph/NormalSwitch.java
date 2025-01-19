package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class NormalSwitch extends GraphPoint implements Switch {
    private final SwitchComponent switchComponent;
    private final GraphPointConnection straight;
    private final GraphPointConnection diverging;

    public NormalSwitch(String name, SwitchComponent switchComponent, GraphPointConnection straight, GraphPointConnection diverging) {
        super(name);
        this.switchComponent = switchComponent;
        this.straight = straight;
        this.diverging = diverging;
    }

    public void switchToConnectPoints(GraphPoint point1, GraphPoint point2) {
        if (straight.connects(point1, point2)) {
            switchComponent.setStraight();
        } else if (diverging.connects(point1, point2)) {
            switchComponent.setDiverging();
        } else {
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }

    public boolean checkIfSwitchConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return straight.connects(point1, point2) || diverging.connects(point1, point2);
    }
}
