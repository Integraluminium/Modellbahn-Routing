package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.List;

public class Route {
    private final List<RoutingAction> actionList;

    public Route(List<RoutingAction> actionList) {
        this.actionList = actionList;
    }

    public void driveRoute(Locomotive loc) {
        for (RoutingAction action : actionList) {
            action.performAction();
        }
    }
}
