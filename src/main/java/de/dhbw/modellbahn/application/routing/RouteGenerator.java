package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.locomotive.Speed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        // TODO handle case: only two edges provided
        DirectedDistanceEdge previousEdge = routingEdges.removeFirst();
        while (routingEdges.size() > 1) {
            DirectedDistanceEdge currentEdge = routingEdges.removeFirst();
            DirectedDistanceEdge nextEdge = routingEdges.getFirst();
            
            previousEdge = currentEdge;
        }

        routingActions.add(new LocSpeedAction(new Speed(0)));
        return new Route(routingActions);
    }

}
