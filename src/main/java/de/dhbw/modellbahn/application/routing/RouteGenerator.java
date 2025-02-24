package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.Speed;

import java.util.*;

public class RouteGenerator {
    private final Locomotive loc;
    private final List<DirectedDistanceEdge> routingEdges;
    private boolean alreadyDecelerated = false;
    private long currentWaitTime = 0;
    private long distanceSum = 0;

    public RouteGenerator(List<DirectedDistanceEdge> routingEdges, Locomotive loc) {
        this.routingEdges = Collections.unmodifiableList(routingEdges);
        this.loc = loc;
    }

    public Route generateRoute() throws PathNotPossibleException {
        if (this.routingEdges.size() < 2) {
            throw new PathNotPossibleException("Not enough Nodes provided to calculate route.");
        }
        this.distanceSum = this.routingEdges.stream().mapToLong(edge -> edge.distance().value()).sum();
        if (this.distanceSum < this.loc.getAccelerationDistance().value() + this.loc.getDecelerationDistance().value()) {
            throw new PathNotPossibleException("Path is shorter than the minimal distance.");
        }

        this.alreadyDecelerated = false;
        this.currentWaitTime = 0;
        this.distanceSum = 0;
        List<RoutingAction> routingActions = new ArrayList<>();
        routingActions.add(new LocSpeedAction(this.loc, new Speed(100)));

        routingActions.addAll(generateActions());

        return new Route(routingActions);
    }

    private List<RoutingAction> generateActions() {
        List<RoutingAction> returnActions = new ArrayList<>();

        if (this.routingEdges.size() == 2) {
            returnActions.addAll(generateActionsForTwoNodes());
        } else {
            returnActions.addAll(generateActionsForMoreThanTwoNodes());
        }

        if (!this.alreadyDecelerated) {
            long decelerationWaitTime = calculateDecelerationTime() - this.currentWaitTime;
            returnActions.add(new WaitAction(decelerationWaitTime));
            this.currentWaitTime += decelerationWaitTime;
            returnActions.add(new LocSpeedAction(this.loc, new Speed(0)));
        }

        return returnActions;
    }

    private List<RoutingAction> generateActionsForMoreThanTwoNodes() {
        List<RoutingAction> returnActions = new ArrayList<>();
        Distance currentDistance = new Distance(0);
        DirectedDistanceEdge previousEdge = this.routingEdges.removeFirst();
        while (this.routingEdges.size() > 1) {
            DirectedDistanceEdge currentEdge = this.routingEdges.removeFirst();
            DirectedDistanceEdge nextEdge = this.routingEdges.getFirst();
            currentDistance = currentDistance.add(currentEdge.distance());

            Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateActionIfNecessary(previousEdge.node().getPoint(), currentEdge, nextEdge.node().getPoint());
            if (switchAction.isPresent()) {
                returnActions.addAll(generateSwitchActions(switchAction.get(), currentDistance));
            }

            previousEdge = currentEdge;
        }

        DirectedDistanceEdge lastNode = this.routingEdges.getFirst();
        if (lastNode.node().getPoint() instanceof Switch) {
            returnActions.addAll(generateActionsForLastSwitch(previousEdge, lastNode, currentDistance));
        }
        return returnActions;
    }

    private List<RoutingAction> generateActionsForLastSwitch(DirectedDistanceEdge previousEdge, DirectedDistanceEdge switchEdge, Distance currentDistance) {
        Switch switchNode = (Switch) switchEdge.node().getPoint();
        GraphPoint endNode = switchNode.getPointThatCanConnectThisPoint(switchEdge.node().getPoint());
        //TODO facing direction must be updated in loc
        Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateActionIfNecessary(previousEdge.node().getPoint(), switchEdge, endNode);
        if (switchAction.isEmpty()) {
            throw new RuntimeException("SwitchAction should not be empty.");
        }
        return generateSwitchActions(switchAction.get(), currentDistance);
    }

    private List<RoutingAction> generateActionsForTwoNodes() {
        DirectedDistanceEdge firstEdge = this.routingEdges.removeFirst();
        DirectedDistanceEdge secondEdge = this.routingEdges.getFirst();
        List<RoutingAction> returnActions = new ArrayList<>();
        if (secondEdge.node().getPoint() instanceof Switch) {
            returnActions.addAll(generateActionsForLastSwitch(firstEdge, secondEdge, new Distance(0)));
        }
        return returnActions;
    }

    private List<RoutingAction> generateSwitchActions(ChangeSwitchStateAction switchAction, Distance currentDistance) {
        List<RoutingAction> returnActions = new ArrayList<>();
        long waitTime = calculateWaitTimeForSwitch(currentDistance, new Distance(300));

        long decelerationTime = calculateDecelerationTime();
        if (!this.alreadyDecelerated && this.currentWaitTime + waitTime > decelerationTime) {
            long decelerationWaitTime = decelerationTime - this.currentWaitTime;
            returnActions.add(new WaitAction(decelerationWaitTime));
            this.currentWaitTime += decelerationWaitTime;
            returnActions.add(new LocSpeedAction(this.loc, new Speed(0)));
            waitTime -= decelerationWaitTime;
            this.alreadyDecelerated = true;
        }

        returnActions.add(new WaitAction(waitTime));
        this.currentWaitTime += waitTime;
        returnActions.add(switchAction);
        return returnActions;
    }

    private Optional<ChangeSwitchStateAction> generateSwitchStateActionIfNecessary(GraphPoint previousPoint, DirectedDistanceEdge currentEdge, GraphPoint nextPoint) {
        if (!(currentEdge.node().getPoint() instanceof Switch)) {
            return Optional.empty();
        }
        return Optional.of(new ChangeSwitchStateAction((Switch) currentEdge.node().getPoint(), previousPoint, nextPoint));
    }

    private long calculateDecelerationTime() {
        long decelerationDistance = this.distanceSum - this.loc.getDecelerationDistance().value() - this.loc.getAccelerationDistance().value();
        long maxSpeedTime = (long) (decelerationDistance / this.loc.getMaxSpeed().value());
        return maxSpeedTime + this.loc.getAccelerationTime();
    }

    private long calculateWaitTimeForSwitch(Distance currentDistance, Distance pufferDistance) {
        int absolutePufferDistance = currentDistance.value() - pufferDistance.value();
        if (absolutePufferDistance <= this.loc.getAccelerationDistance().value()) {
            return 0;
        }
        long absolutePufferWaitTime = (long) (absolutePufferDistance / this.loc.getMaxSpeed().value());
        return absolutePufferWaitTime - this.currentWaitTime;
    }

}
