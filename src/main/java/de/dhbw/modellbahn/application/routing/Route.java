package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.application.routing.actions.RoutingAction;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class Route {
    private static final Logger logger = Logger.getLogger(Route.class.getSimpleName());
    private final Locomotive loc;
    private final List<RoutingAction> actionList;
    private final GraphPoint newPosition;
    private final GraphPoint newFacingDirection;
    private final long estimatedTime;

    public Route(Locomotive loc, List<RoutingAction> actionList, GraphPoint newPosition, GraphPoint newFacingDirection, long estimatedTime) {
        this.loc = loc;
        this.actionList = Collections.unmodifiableList(actionList);
        this.newPosition = newPosition;
        this.newFacingDirection = newFacingDirection;
        this.estimatedTime = estimatedTime;
    }

    public Route addAction(RoutingAction action) {
        List<RoutingAction> newActionList = new java.util.ArrayList<>(getActionList());
        newActionList.add(action);
        return new Route(loc, newActionList, newPosition, newFacingDirection, estimatedTime);
    }

    public Route addAction(RoutingAction action, int index) {
        List<RoutingAction> newActionList = new java.util.ArrayList<>(getActionList());
        newActionList.add(index, action);
        return new Route(loc, newActionList, newPosition, newFacingDirection, estimatedTime);
    }

    public Route addRoute(Route newRoute) {
        if (!this.getLoc().equals(newRoute.getLoc())) {
            throw new IllegalArgumentException("Route loc does not match route loc");
        }

        List<RoutingAction> combinedActionList = new java.util.ArrayList<>(this.getActionList());
        combinedActionList.addAll(newRoute.getActionList());
        long combinedTime = estimatedTime + newRoute.getEstimatedTime();
        return new Route(loc, combinedActionList, newRoute.getNewPosition(), newRoute.getNewFacingDirection(), combinedTime);
    }

    public void driveRoute() {
        int step = 1;
        int maxSteps = actionList.size();
        for (RoutingAction action : actionList) {
            logger.info("Performing action[" + step++ + "/" + maxSteps + "]: " + action + " for loc: " + loc.getLocId());
            action.performAction();
            // NOTE: Maybe the action should be performed at correct time
            //  With some feedback from the hardware there could be a check if the action was successful
            //  further more: position should be present at all time enabling routing of multiple trains
        }
        this.getLoc().setCurrentPosition(this.getNewPosition());
        this.getLoc().setCurrentFacingDirection(this.getNewFacingDirection());
    }

    public List<RoutingAction> getActionList() {
        return actionList;
    }

    public long getEstimatedTime() {
        return estimatedTime;
    }

    public GraphPoint getNewPosition() {
        return newPosition;
    }

    protected GraphPoint getNewFacingDirection() {
        return newFacingDirection;
    }

    protected Locomotive getLoc() {
        return loc;
    }
}
