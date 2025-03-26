package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.actions.WaitAction;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.LocomotiveInfo;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializedPolyTrainRouting extends AbstractMonoTrainRoutingStrategy implements TrainRoutingStrategy {
    @Override
    public Map<Locomotive, Route> generateRoutes(final RoutingContext context) throws PathNotPossibleException {
        Map<Locomotive, Route> routes = new HashMap<>();
        long startDelay = 0;
        Map<Locomotive, LocomotiveInfo> infoMap = context.locomotiveInfoMap();

        de.dhbw.modellbahn.domain.graph.Graph graph = context.domainGraph();

        for (Map.Entry<Locomotive, LocomotiveInfo> entry : infoMap.entrySet()) {
            Locomotive currentLoc = entry.getKey();
            LocomotiveInfo locInfo = entry.getValue();

            // Skip locomotives that are already at their destination
            if (!needsRouting(currentLoc, locInfo)) {
                continue;
            }

            List<GraphPoint> blockedPoints = determineBlockedPoints(infoMap.values());

            // Include points of locomotives already routed (as they will be there)
            routes.keySet().forEach(routedLoc ->
                    blockedPoints.add(routes.get(routedLoc).getNewPosition()));

            blockedPoints.remove(currentLoc.getCurrentPosition());

            Graph<DirectedNode, DefaultWeightedEdge> routingGraph = mapGraphToRoutingGraph(graph, blockedPoints, context.considerHeight(), context.considerElectrification());

            Route route = generateRouteForLocomotive(routingGraph, currentLoc, locInfo, context.routingAlgorithm());
            route = route.addAction(new WaitAction(startDelay), 0);

            startDelay += route.getEstimatedTime();
            routes.put(currentLoc, route);
        }
        return routes;
    }
}
