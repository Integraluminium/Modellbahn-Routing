package de.dhbw.modellbahn.domain.routing;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

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
    public void performAction(Locomotive loc) {
        switchComponent.switchToConnectPoints(point1, point2);
    }
}
