package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.LocomotiveInfo;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.GraphToRoutingGraphMapper;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public abstract class AbstractMonoTrainRoutingStrategy implements TrainRoutingStrategy {
    protected static final Logger logger = Logger.getLogger(Route.class.getSimpleName());

    /**
     * Creates a routing graph considering blocked points
     */
    protected static org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> mapGraphToRoutingGraph(
            de.dhbw.modellbahn.domain.graph.Graph graph,
            List<GraphPoint> blockedPoints,
            boolean considerHeight,
            boolean considerElectrification
    ) {
        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO consider height

        logger.info("Blocked points: " + blockedPoints);
        return mapper.mapGraphToJGraphT(graph, considerElectrification, blockedPoints);
    }

    /**
     * Determines which points are blocked by stationary locomotives
     */
    protected List<GraphPoint> determineBlockedPoints(Collection<LocomotiveInfo> locomotives) {
        return locomotives.stream()
                .filter(info -> needsRouting(info.getLoc(), info))
                .map(info -> info.getLoc().getCurrentPosition())
                .collect(Collectors.toList());
    }

    /**
     * Generates a route for a single locomotive
     */
    protected Route generateRouteForLocomotive(
            org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph,
            Locomotive locomotive,
            LocomotiveInfo info,
            RoutingAlgorithm algorithm) throws PathNotPossibleException {

        MonoTrainRouteGeneratorJGraphT generator =
                new MonoTrainRouteGeneratorJGraphT(algorithm, routingGraph);

        Optional<GraphPoint> facingDirection = info.getDestinationFacingDirection();

        if (facingDirection.isPresent()) {
            return generator.generateRoute(
                    locomotive,
                    info.getDestination(),
                    facingDirection.get(),
                    info.getOptimization()
            );
        } else {
            return generator.generateRoute(
                    locomotive,
                    info.getDestination(),
                    info.getOptimization()
            );
        }
    }

    /**
     * Checks if a locomotive needs routing
     */
    protected boolean needsRouting(Locomotive locomotive, LocomotiveInfo info) {
        return !info.getDestination().equals(locomotive.getCurrentPosition());
    }
}