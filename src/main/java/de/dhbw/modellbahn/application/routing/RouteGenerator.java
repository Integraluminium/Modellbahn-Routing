package de.dhbw.modellbahn.application.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteGenerator {
    private final List<DirectedWeightedEdge> routingEdges;

    public RouteGenerator(List<DirectedWeightedEdge> routingEdges) {
        this.routingEdges = Collections.unmodifiableList(routingEdges);
    }

    public Route generateRoute() {
        final List<Weight> weightList = new ArrayList<>();
        List<RoutingAction> routingActions = routingEdges.stream().map(edge -> {
            weightList.add(edge.weight());
            //TODO return RoutingAction
        }).toList();

        double sum = weightList.stream().mapToDouble(Weight::value).sum();

        // TODO return new Route();
        return null;
    }
}
