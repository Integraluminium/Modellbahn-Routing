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

public class SerializedPolyTrainRouting extends AbstractTrainRoutingStrategy implements TrainRoutingStrategy {
    @Override
    public Map<Locomotive, Route> generateRoutes(final RoutingContext context) throws PathNotPossibleException {
        Map<Locomotive, Route> routes = new HashMap<>();
        long startDelay = 0;
        Map<Locomotive, LocomotiveInfo> infoMap = context.locomotiveInfoMap();

        for (LocomotiveInfo locInfo : infoMap.values()) {
            List<GraphPoint> blockedPoints = getBlockedPoints(infoMap, routes, locInfo.getLoc());
            Route route = getRouteForSingleLocomotive(context, locInfo, blockedPoints, startDelay);

            if (route == null) continue;

            startDelay += route.getEstimatedTime();
            routes.put(locInfo.getLoc(), route);
        }
        return routes;
    }

    private Route getRouteForSingleLocomotive(final RoutingContext context, final LocomotiveInfo locInfo, final List<GraphPoint> blockedPoints, final long startDelay) throws PathNotPossibleException {
        Locomotive currentLoc = locInfo.getLoc();

        // Skip locomotives that are already at their destination
        if (!locConsideredInRouting(locInfo)) {
            return null;
        }

        Graph<DirectedNode, DefaultWeightedEdge> routingGraph = mapGraphToRoutingGraph(
                context.domainGraph(),
                blockedPoints,
                context.considerHeight(),
                isConsiderElectrification(context, currentLoc)
        );

        Route route = generateRouteForLocomotive(routingGraph, currentLoc, locInfo, context.routingAlgorithm());
        route = route.addAction(new WaitAction(startDelay), 0);
        return route;
    }

    private List<GraphPoint> getBlockedPoints(final Map<Locomotive, LocomotiveInfo> infoMap, final Map<Locomotive, Route> routes, final Locomotive currentLoc) {
        List<GraphPoint> blockedPoints = getCurrentPositionsOfLocsConsideredInRouting(infoMap.values());

        // change points of locomotives that already have been routed (as they will be there)
        routes.keySet().forEach(routedLoc -> {
                    LocomotiveInfo routedLocInfo = infoMap.get(routedLoc);
                    blockedPoints.add(routedLocInfo.getDestination());
                    blockedPoints.remove(routedLocInfo.getLoc().getCurrentPosition());
                }
        );
        blockedPoints.remove(currentLoc.getCurrentPosition());
        return blockedPoints;
    }
}
