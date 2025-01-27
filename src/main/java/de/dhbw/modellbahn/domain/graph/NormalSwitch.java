package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class NormalSwitch extends GraphPoint implements Switch {
    private final SwitchComponent switchComponent;

    private final GraphPoint root;
    private final GraphPoint straight;
    private final GraphPoint turnout;

    public NormalSwitch(String name, SwitchComponent switchComponent, GraphPoint root, GraphPoint straight, GraphPoint turnout) {
        super(name);
        this.switchComponent = switchComponent;
        this.root = root;
        this.straight = straight;
        this.turnout = turnout;
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

    public void switchToConnectToPoint(GraphPoint point) {

    }

    public boolean checkIfSwitchConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return straight.connects(point1, point2) || diverging.connects(point1, point2);
    }

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        return null;
    }
}
