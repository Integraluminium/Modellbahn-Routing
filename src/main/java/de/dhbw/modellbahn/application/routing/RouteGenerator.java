package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.locomotive.Speed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RouteGenerator {
    private final List<DirectedDistanceEdge> routingEdges;
    private final Distance minimalDistance;

    public RouteGenerator(List<DirectedDistanceEdge> routingEdges, Distance minimalDistance) {
        this.routingEdges = Collections.unmodifiableList(routingEdges);
        this.minimalDistance = minimalDistance;
    }

    public Route generateRoute() throws PathNotPossibleException {
        double sum = routingEdges.stream().mapToDouble(edge -> edge.distance().value()).sum();
        if (sum < minimalDistance.value()) {
            throw new PathNotPossibleException("Path is shorter than the minimal distance.");
        }

        List<RoutingAction> routingActions = new ArrayList<>();
        routingActions.add(new LocSpeedAction(new Speed(100)));

        generateActions(routingActions);

        routingActions.add(new LocSpeedAction(new Speed(0)));
        return new Route(routingActions);
    }

    private void generateActions(List<RoutingAction> routingActions) {
        // TODO handle case: only two edges provided
        Distance currentDistance = new Distance(0);
        double currentWaitTime = 0;
        DirectedDistanceEdge previousEdge = routingEdges.removeFirst();
        while (routingEdges.size() > 1) {
            DirectedDistanceEdge currentEdge = routingEdges.removeFirst();
            DirectedDistanceEdge nextEdge = routingEdges.getFirst();
            currentDistance = currentDistance.add(currentEdge.distance());

            Optional<ChangeSwitchStateAction> switchAction = generateSwitchStateIfNecessary(previousEdge, currentEdge, nextEdge);
            if(switchAction.isPresent()){
                WaitAction waitAction = new WaitAction(currentDistance, new Distance(300));
                routingActions.add(waitAction);
                routingActions.add(switchAction.get());
            }

            previousEdge = currentEdge;
        }
    }

    private Optional<ChangeSwitchStateAction> generateSwitchStateIfNecessary(DirectedDistanceEdge previousEdge, DirectedDistanceEdge currentEdge, DirectedDistanceEdge nextEdge) {
        if(!(currentEdge.node().getPoint() instanceof Switch)){
            return Optional.empty();
        }
        return Optional.of(new ChangeSwitchStateAction((Switch) currentEdge.node(), previousEdge.node().getPoint(), nextEdge.node().getPoint()));
    }

}
