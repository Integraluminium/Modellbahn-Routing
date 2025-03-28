package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.LocomotiveInfo;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MonoTrainRouting extends AbstractTrainRoutingStrategy implements TrainRoutingStrategy {


    @Override
    public Map<Locomotive, Route> generateRoutes(final RoutingContext routingContext) throws PathNotPossibleException {
        Map<Locomotive, LocomotiveInfo> locomotivesToConsiderInRouting = routingContext.locomotiveInfoMap();
        Locomotive loc = locomotivesToConsiderInRouting.keySet().iterator().next();

        Graph<DirectedNode, DefaultWeightedEdge> routingGraph = mapGraphToRoutingGraph(
                routingContext.domainGraph(),
                getBlockedPoints(locomotivesToConsiderInRouting.values(), loc),
                routingContext.considerHeight(),
                isConsiderElectrification(routingContext, loc)
        );

        Route route = generateRouteForLocomotive(
                routingGraph,
                loc,
                locomotivesToConsiderInRouting.get(loc),
                routingContext.routingAlgorithm()
        );
        return Map.of(loc, route);
    }

    private List<GraphPoint> getBlockedPoints(final Collection<LocomotiveInfo> values, final Locomotive loc) {
        List<GraphPoint> blockedPoints = getCurrentPositionsOfLocsConsideredInRouting(values);
        blockedPoints.remove(loc.getCurrentPosition());
        return blockedPoints;
    }

}
