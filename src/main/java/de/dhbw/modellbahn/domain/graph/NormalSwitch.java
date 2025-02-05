package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class NormalSwitch extends GraphPoint implements Switch {
    private final SwitchComponent switchComponent;

    private final String root;
    private final String straight;
    private final String turnout;

    public NormalSwitch(String name, SwitchComponent switchComponent, String root, String straight, String turnout) {
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

    public boolean checkIfConnectsPoints(GraphPoint point1, GraphPoint point2) {
        return connectsStraight(point1, point2) || connectsDiverging(point1, point2);
    }

    private boolean connectsStraight(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(straight))
                || (point2.equals(root) && point1.equals(straight));
    }

    private boolean connectsDiverging(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root) && point2.equals(turnout))
                || (point2.equals(root) && point1.equals(turnout));
    }

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        if (point.equals(root)) {
            return SwitchSide.IN;
        } else if (point.equals(straight) || point.equals(turnout)) {
            return SwitchSide.OUT;
        } else {
            return SwitchSide.UNDEFINED;
        }
    }
}
