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

public abstract class AbstractTrainRoutingStrategy implements TrainRoutingStrategy {
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
        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper();

        logger.info("Blocked points: " + blockedPoints);
        return mapper.mapGraphToJGraphT(graph, considerElectrification, considerHeight, blockedPoints);
    }

    public static boolean isConsiderElectrification(final RoutingContext context, final Locomotive currentLoc) {
        return context.considerElectrification() && currentLoc.isElectric();
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
     * Determines which points are blocked by stationary locomotives
     */
    protected List<GraphPoint> getCurrentPositionsOfLocsConsideredInRouting(Collection<LocomotiveInfo> locomotives) {
        return locomotives.stream()
                .filter(this::locConsideredInRouting)
                .map(info -> info.getLoc().getCurrentPosition())
                .collect(Collectors.toList());
    }

    /**
     * Checks if a locomotive is already at destination
     *
     * @return ``true`` if the locomotive is not at its destination
     */
    protected boolean locConsideredInRouting(LocomotiveInfo info) {
        return !info.getDestination().equals(info.getLoc().getCurrentPosition());
    }
}