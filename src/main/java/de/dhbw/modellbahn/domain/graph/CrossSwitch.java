package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.SwitchComponent;

public class CrossSwitch extends GraphPoint implements Switch {
    private final SwitchComponent switchComponent;

    private final PointName root1;
    private final PointName root2;
    private final PointName turnout1;
    private final PointName turnout2;

    public CrossSwitch(PointName name, SwitchComponent switchComponent, PointName root1, PointName root2, PointName turnout1, PointName turnout2) {
        super(name);
        this.switchComponent = switchComponent;
        this.root1 = root1;
        this.root2 = root2;
        this.turnout1 = turnout1;
        this.turnout2 = turnout2;
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
        return (point1.equals(root1) && point2.equals(turnout2)) || (point2.equals(root1) && point1.equals(turnout2)) ||
                (point1.equals(root2) && point2.equals(turnout1)) || (point2.equals(root2) && point1.equals(turnout1));
    }

    private boolean connectsDiverging(GraphPoint point1, GraphPoint point2) {
        return (point1.equals(root1) && point2.equals(turnout1)) || (point2.equals(root1) && point1.equals(turnout1)) ||
                (point1.equals(root2) && point2.equals(turnout2)) || (point2.equals(root2) && point1.equals(turnout2));
    }

    public SwitchSide getSwitchSideFromPoint(GraphPoint point) {
        if (point.equals(root1) || point.equals(root2)) {
            return SwitchSide.IN;
        } else if (point.equals(turnout1) || point.equals(turnout2)) {
            return SwitchSide.OUT;
        } else {
            return SwitchSide.UNDEFINED;
        }
    }


}
