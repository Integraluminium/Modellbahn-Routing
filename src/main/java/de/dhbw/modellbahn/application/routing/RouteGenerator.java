package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;

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
        final List<Distance> distanceList = new ArrayList<>();
        List<RoutingAction> routingActions = routingEdges.stream().map(edge -> {
            distanceList.add(edge.distance());
            return generateActionFromNode(edge.node());
        }).toList();

        double sum = distanceList.stream().mapToDouble(Distance::value).sum();
        if (sum < minimalDistance.value()) {
            throw new PathNotPossibleException("Path is shorter than the minimal distance.");
        }

        return new Route(routingActions);
    }

    private RoutingAction generateActionFromNode(DirectedNode node) {
        return null;
    }
}
