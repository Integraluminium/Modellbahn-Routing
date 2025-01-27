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
        if (connectsStraight(point1, point2)) {
            switchComponent.setStraight();
        } else if (connectsDiverging(point1, point2)) {
            switchComponent.setDiverging();
        } else {
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }

    public void switchToConnectToPoint(GraphPoint point) {
        if (point == straight) {
            switchComponent.setStraight();
        } else if (point == turnout) {
            switchComponent.setDiverging();
        } else if (point != root) {
            throw new IllegalArgumentException("Given GraphPoint is not connected witch this switch.");
        }
    }

    public boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return connectsStraight(point1, point2) || connectsDiverging(point1, point2);
    }

    private boolean connectsStraight(GraphPoint point1, GraphPoint point2) {
        return (point1 == root && point2 == straight) || (point2 == root && point1 == straight);
    }

    private boolean connectsDiverging(GraphPoint point1, GraphPoint point2) {
        return (point1 == root && point2 == turnout) || (point2 == root && point1 == turnout);
    }

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        if (point == root) {
            return SwitchSide.IN;
        } else if (point == straight || point == turnout) {
            return SwitchSide.OUT;
        } else {
            return SwitchSide.UNDEFINED;
        }
    }
}
