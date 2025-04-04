package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.application.routing.actions.*;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.directed.graph.WeightedDistanceEdge;
import de.dhbw.modellbahn.domain.events.DomainEventPublisher;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.Signal;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.TrackContact;
import de.dhbw.modellbahn.domain.graph.nodes.switches.Switch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.domain.physical.railway.components.SignalState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Generates a route for a given Locomotive and a list of WeightedDistanceEdges.
 */
public class RouteGenerator {
    private final Locomotive loc;
    private final List<WeightedDistanceEdge> routingEdges;
    private final GraphPoint newFacingDirection;
    private boolean alreadyDecelerated = false;
    private long currentWaitTime = 0;
    private long distanceSum = 0;
    private long estimatedTime = 0;

    /**
     * Constructor for RouteGenerator.
     *
     * @param loc                Locomotive which should drive the route.
     * @param routingEdges       List of WeightedDistanceEdges which represent the route.
     * @param newFacingDirection New facing direction of the Locomotive after the route.
     */
    public RouteGenerator(Locomotive loc, List<WeightedDistanceEdge> routingEdges, GraphPoint newFacingDirection) {
        this.loc = loc;
        this.routingEdges = new ArrayList<>(routingEdges);
        this.newFacingDirection = newFacingDirection;
    }

    /**
     * Generates a route for the given Locomotive and the list of WeightedDistanceEdges.
     * Multiple calls generate different instances of the same Route.
     *
     * @return Route for the Locomotive.
     * @throws PathNotPossibleException If the path is not possible.
     */
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
        Speed speed = new Speed(100);
        routingActions.add(
                new LocSpeedAction(this.loc, speed)
        );  // TODO: Parameterize speed

        routingActions.addAll(generateActions());

        return new Route(this.loc, routingActions, newPosition, newFacingDirection, this.estimatedTime);
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
            this.estimatedTime = this.currentWaitTime + this.loc.getDecelerationTime();
            Speed speed = new Speed(0);
            returnActions.add(new LocSpeedAction(this.loc, speed));
        }
        returnActions.add(new WaitAction(this.estimatedTime - this.currentWaitTime));
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

            // Switch action
            GraphPoint currentPoint = currentEdge.point();
            if (currentPoint instanceof Switch) { // Switch
                Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateActionIfNecessary(previousEdge.point(), currentPoint, nextEdge.point());
                if (switchAction.isPresent()) {
                    returnActions.addAll(generateSwitchActions(switchAction.get(), currentDistance));
                }

            } else if (currentPoint instanceof Signal) { // Signal action
                // Signal is activated directly after previous component, because no timing needed
                SignalChangeAction signalChangeAction = new SignalChangeAction((Signal) currentPoint, SignalState.CLEAR);
                returnActions.add(signalChangeAction);


            } else if (currentPoint instanceof TrackContact) { // Position verification action
                // Should be executed when locomotive IS exactly at the current component
                long waitTime = calculateWaitTimeForSwitch(currentDistance, new Distance(0));
                WaitAction waitAction = new WaitAction(waitTime);
                DomainEventPublisher eventPublisher = DomainEventPublisher.getInstance();
                PositionVerificationAction verificationAction = new PositionVerificationAction(eventPublisher, loc, (TrackContact) currentPoint);

                returnActions.add(waitAction);
                returnActions.add(verificationAction);

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
        // TODO Parameterize puffer distance

        long decelerationTime = calculateDecelerationTime();
        if (!this.alreadyDecelerated && this.currentWaitTime + waitTime > decelerationTime) {
            long decelerationWaitTime = decelerationTime - this.currentWaitTime;
            returnActions.add(new WaitAction(decelerationWaitTime));
            this.currentWaitTime += decelerationWaitTime;
            Speed speed = new Speed(0);
            returnActions.add(
                    new LocSpeedAction(this.loc, speed)
            );
            waitTime -= decelerationWaitTime;
            this.alreadyDecelerated = true;
            this.estimatedTime = this.currentWaitTime + this.loc.getDecelerationTime();
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

    /**
     * Calculates the wait time, when the train is arrived at the specific component
     *
     * @param currentDistance distance till the current component
     * @param pufferDistance  Distance before the component when the train should switch the component
     * @return the wait time till the action should be performed
     */
    private long calculateWaitTimeForSwitch(Distance currentDistance, Distance pufferDistance) {
        int absolutePufferDistance = currentDistance.value() - pufferDistance.value();
        if (absolutePufferDistance <= this.loc.getAccelerationDistance().value()) {
            return 0;
        }
        long absolutePufferWaitTime = (long) ((absolutePufferDistance - this.loc.getAccelerationDistance().value()) / this.loc.getMaxSpeed().value()) + this.loc.getAccelerationTime();
        return absolutePufferWaitTime - this.currentWaitTime;
    }

}
