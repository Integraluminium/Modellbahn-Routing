package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class CrossSwitch extends GraphPoint implements Switch {
    private final GraphPointConnection straight1;
    private final GraphPointConnection straight2;
    private final GraphPointConnection diverging1;
    private final GraphPointConnection diverging2;

    private final SwitchComponent switchComponent;

    public CrossSwitch(String name, GraphPointConnection straight1, GraphPointConnection straight2, GraphPointConnection diverging1, GraphPointConnection diverging2, SwitchComponent switchComponent) {
        super(name);
        this.straight1 = straight1;
        this.straight2 = straight2;
        this.diverging1 = diverging1;
        this.diverging2 = diverging2;
        this.switchComponent = switchComponent;
    }

    public void switchToConnectPoints(GraphPoint point1, GraphPoint point2) {
        if (straight1.connects(point1, point2) || straight2.connects(point1, point2)) {
            switchComponent.setStraight();
        } else if (diverging1.connects(point1, point2) || diverging2.connects(point1, point2)) {
            switchComponent.setDiverging();
        } else {
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }

    public boolean checkIfSwitchConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return straight1.connects(point1, point2) || straight2.connects(point1, point2)
                || diverging1.connects(point1, point2) || diverging2.connects(point1, point2);
    }


}
