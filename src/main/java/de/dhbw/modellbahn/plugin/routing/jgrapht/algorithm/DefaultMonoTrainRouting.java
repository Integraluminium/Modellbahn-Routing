package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.LocomotiveInfo;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DefaultMonoTrainRouting extends AbstractMonoTrainRoutingStrategy implements TrainRoutingStrategy {

    @Override
    public Map<Locomotive, Route> generateRoutes(final RoutingContext routingContext) throws PathNotPossibleException {
        Map<Locomotive, LocomotiveInfo> locomotivesToConsiderInRouting = routingContext.locomotiveInfoMap();
        RoutingAlgorithm algorithm = routingContext.routingAlgorithm();
        de.dhbw.modellbahn.domain.graph.Graph graph = routingContext.domainGraph();

        Collection<LocomotiveInfo> values = locomotivesToConsiderInRouting.values();
        Locomotive loc = locomotivesToConsiderInRouting.keySet().iterator().next();

        List<GraphPoint> blockedPoints = determineBlockedPoints(values);
        blockedPoints.remove(loc.getCurrentPosition());
        boolean considerOnlyElectrifiedTracks = routingContext.considerElectrification() && loc.isElectric();

        Graph<DirectedNode, DefaultWeightedEdge> routingGraph = mapGraphToRoutingGraph(graph, blockedPoints, routingContext.considerHeight(), considerOnlyElectrifiedTracks);

        LocomotiveInfo locomotiveInfo = locomotivesToConsiderInRouting.get(loc);
        Route route = generateRouteForLocomotive(routingGraph, loc, locomotiveInfo, algorithm);
        return Map.of(loc, route);
    }

}
