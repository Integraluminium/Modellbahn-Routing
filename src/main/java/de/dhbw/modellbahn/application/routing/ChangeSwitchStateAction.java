package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;

public class ChangeSwitchStateAction extends RoutingAction {
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
}
