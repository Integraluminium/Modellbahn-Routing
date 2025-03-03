package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.application.routing.action.ActionFactory;
import de.dhbw.modellbahn.application.routing.action.ChangeSwitchStateAction;
import de.dhbw.modellbahn.application.routing.action.RoutingAction;
import de.dhbw.modellbahn.application.routing.action.WaitAction;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RouteGenerator {
    private final Locomotive loc;
    private final List<WeightedDistanceEdge> routingEdges;
    private final GraphPoint newFacingDirection;
    private final ActionFactory actionFactory;
    private boolean alreadyDecelerated = false;
    private long currentWaitTime = 0;
    private long distanceSum = 0;

    public RouteGenerator(Locomotive loc, List<WeightedDistanceEdge> routingEdges, GraphPoint newFacingDirection) {
        this.loc = loc;
        this.routingEdges = new ArrayList<>(routingEdges);
        this.newFacingDirection = newFacingDirection;
        this.actionFactory = new ActionFactory();
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

        GraphPoint newPosition = this.routingEdges.getLast().point();

        List<RoutingAction> routingActions = new ArrayList<>();
        routingActions.add(
                actionFactory.createLocSpeedAction(this.loc, 100)
        );  // TODO: Parameterize speed

        routingActions.addAll(generateActions());

        return new Route(this.loc, routingActions, newPosition, newFacingDirection);
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
            returnActions.add(this.actionFactory.createLocSpeedAction(this.loc, 0));
        }

        return returnActions;
    }

    private List<RoutingAction> generateActionsForMoreThanTwoNodes() {
        List<RoutingAction> returnActions = new ArrayList<>();
        Distance currentDistance = new Distance(0);
        WeightedDistanceEdge previousEdge = this.routingEdges.removeFirst();
        while (this.routingEdges.size() > 1) {
            WeightedDistanceEdge currentEdge = this.routingEdges.removeFirst();
            WeightedDistanceEdge nextEdge = this.routingEdges.getFirst();
            currentDistance = currentDistance.add(currentEdge.distance());

            Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateActionIfNecessary(previousEdge.point(), currentEdge.point(), nextEdge.point());
            if (switchAction.isPresent()) {
                returnActions.addAll(generateSwitchActions(switchAction.get(), currentDistance));
            }

            previousEdge = currentEdge;
        }

        WeightedDistanceEdge lastEdge = this.routingEdges.getFirst();
        if (lastEdge.point() instanceof Switch) {
            returnActions.addAll(generateActionsForLastSwitch(previousEdge, lastEdge, currentDistance));
        }
        return returnActions;
    }

    private List<RoutingAction> generateActionsForLastSwitch(WeightedDistanceEdge previousEdge, WeightedDistanceEdge switchEdge, Distance currentDistance) {
        Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateActionIfNecessary(previousEdge.point(), switchEdge.point(), this.newFacingDirection);
        if (switchAction.isEmpty()) {
            throw new RuntimeException("SwitchAction should not be empty.");
        }
        return generateSwitchActions(switchAction.get(), currentDistance);
    }

    private List<RoutingAction> generateActionsForTwoNodes() {
        WeightedDistanceEdge firstEdge = this.routingEdges.removeFirst();
        WeightedDistanceEdge secondEdge = this.routingEdges.getFirst();
        List<RoutingAction> returnActions = new ArrayList<>();
        if (secondEdge.point() instanceof Switch) {
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
            returnActions.add(
                    actionFactory.createLocSpeedAction(this.loc, 0)
            );
            waitTime -= decelerationWaitTime;
            this.alreadyDecelerated = true;
        }

        returnActions.add(new WaitAction(waitTime));
        this.currentWaitTime += waitTime;
        returnActions.add(switchAction);
        return returnActions;
    }

    private Optional<ChangeSwitchStateAction> generateSwitchStateActionIfNecessary(GraphPoint previousPoint, GraphPoint currentPoint, GraphPoint nextPoint) {
        if (!(currentPoint instanceof Switch)) {
            return Optional.empty();
        }
        return Optional.of(new ChangeSwitchStateAction((Switch) currentPoint, previousPoint, nextPoint));
    }

    private long calculateDecelerationTime() {
        long decelerationMaxSpeedDistance = this.distanceSum - this.loc.getDecelerationDistance().value() - this.loc.getAccelerationDistance().value();
        long maxSpeedTime = (long) (decelerationMaxSpeedDistance / this.loc.getMaxSpeed().value());
        return maxSpeedTime + this.loc.getAccelerationTime();
    }

    private long calculateWaitTimeForSwitch(Distance currentDistance, Distance pufferDistance) {
        int absolutePufferDistance = currentDistance.value() - pufferDistance.value();
        if (absolutePufferDistance <= this.loc.getAccelerationDistance().value()) {
            return 0;
        }
        long absolutePufferWaitTime = (long) ((absolutePufferDistance - this.loc.getAccelerationDistance().value()) / this.loc.getMaxSpeed().value()) + this.loc.getAccelerationTime();
        return absolutePufferWaitTime - this.currentWaitTime;
    }

}
