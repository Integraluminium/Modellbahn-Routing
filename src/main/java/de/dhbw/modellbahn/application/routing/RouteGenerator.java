package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.Speed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RouteGenerator {
    private final Locomotive loc;
    private final List<DirectedDistanceEdge> routingEdges;
    private boolean alreadyDecelerated = false;
    private long currentWaitTime = 0;

    public RouteGenerator(List<DirectedDistanceEdge> routingEdges, Locomotive loc) {
        this.routingEdges = Collections.unmodifiableList(routingEdges);
        this.loc = loc;
    }

    public Route generateRoute() throws PathNotPossibleException {
        double distanceSum = this.routingEdges.stream().mapToDouble(edge -> edge.distance().value()).sum();
        if (distanceSum < this.loc.getAccelerationDistance().value() + this.loc.getDecelerationDistance().value()) {
            throw new PathNotPossibleException("Path is shorter than the minimal distance.");
        }
        this.alreadyDecelerated = false;
        this.currentWaitTime = 0;
        List<RoutingAction> routingActions = new ArrayList<>();
        routingActions.add(new LocSpeedAction(this.loc, new Speed(100)));

        routingActions.addAll(generateActions(distanceSum));

        return new Route(routingActions);
    }

    private List<RoutingAction> generateActions(double distanceSum) {
        List<RoutingAction> returnActions = new ArrayList<>();
        // TODO handle case: only two edges provided
        Distance currentDistance = new Distance(0);
        long decelerationTime = calculateDecelerationTime(distanceSum);
        DirectedDistanceEdge previousEdge = this.routingEdges.removeFirst();
        while (this.routingEdges.size() > 1) {
            DirectedDistanceEdge currentEdge = this.routingEdges.removeFirst();
            DirectedDistanceEdge nextEdge = this.routingEdges.getFirst();
            currentDistance = currentDistance.add(currentEdge.distance());

            Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateActionIfNecessary(previousEdge, currentEdge, nextEdge);
            if (switchAction.isPresent()) {
                returnActions.addAll(generateSwitchActions(switchAction.get(), currentDistance, decelerationTime));
            }

            previousEdge = currentEdge;
        }

        //TODO handle case: last node is a switch

        if (!this.alreadyDecelerated) {
            long decelerationWaitTime = decelerationTime - this.currentWaitTime;
            returnActions.add(new WaitAction(decelerationWaitTime));
            returnActions.add(new LocSpeedAction(this.loc, new Speed(0)));
        }

        return returnActions;
    }

    private List<RoutingAction> generateSwitchActions(ChangeSwitchStateAction switchAction, Distance currentDistance, long decelerationTime) {
        List<RoutingAction> returnActions = new ArrayList<>();
        long waitTime = calculateWaitTimeForSwitch(currentDistance, new Distance(300));

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

    private Optional<ChangeSwitchStateAction> generateSwitchStateActionIfNecessary(DirectedDistanceEdge previousEdge, DirectedDistanceEdge currentEdge, DirectedDistanceEdge nextEdge) {
        if (!(currentEdge.node().getPoint() instanceof Switch)) {
            return Optional.empty();
        }
        return Optional.of(new ChangeSwitchStateAction((Switch) currentEdge.node(), previousEdge.node().getPoint(), nextEdge.node().getPoint()));
    }

    private long calculateDecelerationTime(double distanceSum) {
        double decelerationDistance = distanceSum - this.loc.getDecelerationDistance().value() - this.loc.getAccelerationDistance().value();
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
