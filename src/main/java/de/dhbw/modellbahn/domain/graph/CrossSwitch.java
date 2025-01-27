package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class CrossSwitch extends GraphPoint implements Switch {
    private final SwitchComponent switchComponent;

    private final GraphPoint root1;
    private final GraphPoint root2;
    private final GraphPoint turnout1;
    private final GraphPoint turnout2;

    public CrossSwitch(String name, SwitchComponent switchComponent, GraphPoint root1, GraphPoint root2, GraphPoint turnout1, GraphPoint turnout2) {
        super(name);
        this.switchComponent = switchComponent;
        this.root1 = root1;
        this.root2 = root2;
        this.turnout1 = turnout1;
        this.turnout2 = turnout2;
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

    public void switchToConnectToPoint(GraphPoint point) {

    }

    public boolean checkIfSwitchConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return straight1.connects(point1, point2) || straight2.connects(point1, point2)
                || diverging1.connects(point1, point2) || diverging2.connects(point1, point2);
    }

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        return null;
    }


}
