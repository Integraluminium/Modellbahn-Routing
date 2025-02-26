package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.Collections;
import java.util.List;

public class Route {
    private final Locomotive loc;
    private final List<RoutingAction> actionList;
    private final GraphPoint newPosition;
    private final GraphPoint newFacingDirection;

    public Route(Locomotive loc, List<RoutingAction> actionList, GraphPoint newPosition, GraphPoint newFacingDirection) {
        this.loc = loc;
        this.actionList = Collections.unmodifiableList(actionList);
        this.newPosition = newPosition;
        this.newFacingDirection = newFacingDirection;
    }

    public void driveRoute() {
        for (RoutingAction action : actionList) {
            action.performAction();
        }
        this.loc.setCurrentPosition(this.newPosition);
        this.loc.setCurrentFacingDirection(this.newFacingDirection);
    }

    public List<RoutingAction> getActionList() {
        return actionList;
    }
}
