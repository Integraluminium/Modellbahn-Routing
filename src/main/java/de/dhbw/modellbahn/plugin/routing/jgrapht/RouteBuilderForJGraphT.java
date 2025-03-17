package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RouteBuilder;
import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.GraphToRoutingGraphMapper;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;

public class RouteBuilderForJGraphT implements RouteBuilder {
    private final HashMap<Locomotive, LocomotiveInfo> locomotivesToConsiderInRouting;
    private final Graph graph;
    private final HashMap<Locomotive, Route> routes;
    private boolean considerElectrification;
    private boolean considerHeight;
    private RoutingAlgorithm algorithm;


    public RouteBuilderForJGraphT(Graph graph) {
        this.locomotivesToConsiderInRouting = new HashMap<>();
        this.routes = new HashMap<>();
        this.considerElectrification = false;
        this.considerHeight = false;
        this.graph = graph;
        this.algorithm = RoutingAlgorithm.DIJKSTRA;
    }

    @Override
    public RouteBuilder addLocomotive(final Locomotive loc) {
        this.locomotivesToConsiderInRouting.put(loc, new LocomotiveInfo(loc));
        return this;
    }

    @Override
    public RouteBuilder setDestinationForLoc(final Locomotive loc, final GraphPoint destination) {
        this.locomotivesToConsiderInRouting.get(loc).setDestination(destination);
        return this;
    }

    @Override
    public RouteBuilder setLocFacingDirectionForDestination(final Locomotive loc, final GraphPoint facingDirection) {
        this.locomotivesToConsiderInRouting.get(loc).setDestinationFacingDirection(facingDirection);
        return this;
    }

    @Override
    public RouteBuilder considerElectrification(boolean toConsider) {
        this.considerElectrification = true;
        return this;
    }

    @Override
    public RouteBuilder considerHeight(boolean toConsider) {
        this.considerHeight = true;
        return this;
    }

    /**
     * @inheritDoc <toModifySystem><i>CURRENTLY UNSUPPORTED</i></toModifySystem>
     */
    @Override
    public RouteBuilder setRouteOptimization(final Locomotive loc, final RoutingOptimization optimization) {
        this.locomotivesToConsiderInRouting.get(loc).setOptimisation(optimization);
        return this;
    }

    @Override
    public void generateRoute() throws PathNotPossibleException {
        if (getAmountOfLocomotivesToConsider() == 0) {
            throw new IllegalStateException("No locomotives to consider in routing.");
        }
        // TODO consider Height
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph = mapGraphToRoutingGraph(considerHeight);

        if (getAmountOfLocomotivesToConsider() > 1) {
            generateRouteForMultipleLocomotives(routingGraph);
        } else {
            generateRouteForSingleLocomotive(routingGraph);

        }
    }

    @Override
    public Route getRouteForLoc(final Locomotive loc) {
        if (!locomotivesToConsiderInRouting.containsKey(loc)) {
            throw new IllegalArgumentException("Locomotive is not considered in routing.");
        }
        if (!routes.containsKey(loc)) {
            throw new IllegalStateException("Route for locomotive not generated yet.");
        }
        return routes.get(loc);
    }

    @Override
    public Iterable<Locomotive> getLocomotivesWithRoute() {
        return this.routes.keySet();
    }

    private void generateRouteForMultipleLocomotives(final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph) throws PathNotPossibleException {
        throw new UnsupportedOperationException("Routing of multiple locomotives is not implemented yet.");
    }

    private void generateRouteForSingleLocomotive(final org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph) throws PathNotPossibleException {
        Locomotive locomotive = locomotivesToConsiderInRouting.keySet().iterator().next();
        // TODO consider electrification IF locomotive is electric
        MonoTrainRoutingJGraphT monoTrainRoutingJGraphT = new MonoTrainRoutingJGraphT(algorithm, routingGraph);

        LocomotiveInfo locomotiveInfo = locomotivesToConsiderInRouting.get(locomotive);

        Route route;
        if (locomotiveInfo.getDestinationFacingDirection().isPresent()) {
            route = monoTrainRoutingJGraphT.generateRoute(locomotive, locomotiveInfo.getDestination(), locomotiveInfo.getDestinationFacingDirection().get(), locomotiveInfo.getOptimisation());
        } else {
            route = monoTrainRoutingJGraphT.generateRoute(
                    locomotive,
                    locomotiveInfo.getDestination(),
                    locomotiveInfo.getOptimisation()
            );
        }
        routes.put(locomotive, route);
    }

    private org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> mapGraphToRoutingGraph(boolean considerHeight) {
        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO consider height and electrification
        return mapper.mapGraphToJGraphT(graph);
    }

    private long getAmountOfLocomotivesToConsider() {
        return this.locomotivesToConsiderInRouting.values().stream().filter(loc -> loc.getDestination() != null).count();
    }

    public RouteBuilder setRoutingAlgorithm(final RoutingAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

}
