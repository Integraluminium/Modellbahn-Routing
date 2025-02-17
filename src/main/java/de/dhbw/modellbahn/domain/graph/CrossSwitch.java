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

    public PointSide getSwitchSideFromPoint(GraphPoint point) {
        if (point == root1 || point == root2) {
            return PointSide.IN;
        } else if (point == turnout1 || point == turnout2) {
            return PointSide.OUT;
        } else {
            throw new IllegalArgumentException("Point is not connected to this switch.");
        }
    }

    private boolean connectsStraight(GraphPoint point1, GraphPoint point2) {
        return (point1 == root1 && point2 == turnout2) || (point2 == root1 && point1 == turnout2) ||
                (point1 == root2 && point2 == turnout1) || (point2 == root2 && point1 == turnout1);
    }

    private boolean connectsDiverging(GraphPoint point1, GraphPoint point2) {
        return (point1 == root1 && point2 == turnout1) || (point2 == root1 && point1 == turnout1) ||
                (point1 == root2 && point2 == turnout2) || (point2 == root2 && point1 == turnout2);
    }


}
