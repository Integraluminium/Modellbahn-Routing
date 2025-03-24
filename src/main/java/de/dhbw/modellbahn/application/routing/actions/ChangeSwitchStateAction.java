package de.dhbw.modellbahn.application.routing.actions;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.Switch;

public class ChangeSwitchStateAction implements RoutingAction {
    private final Switch switchComponent;
    private final GraphPoint point1;
    private final GraphPoint point2;


    public ChangeSwitchStateAction(Switch switchComponent, GraphPoint point1, GraphPoint point2) {
        this.switchComponent = switchComponent;
        this.point1 = point1;
        this.point2 = point2;
    }

    @Override
    public void performAction() {
        switchComponent.switchToConnectPoints(point1, point2);
    }

    public GraphPoint getPoint1() {
        return point1;
    }

    public GraphPoint getPoint2() {
        return point2;
    }

    @Override
    public String toString() {
        return "ChangeSwitchStateAction{" +
               switchComponent.toString() +
               ": point1=" + point1.getName().name() +
               ", point2=" + point2.getName().name() +
               '}';
    }
}
