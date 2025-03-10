package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RouteGenerator;
import de.dhbw.modellbahn.application.RoutingAlgorithm;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.old.GraphToRoutingGraphMapper;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;

public class RouteGeneratorForJGraphT implements RouteGenerator {
    private final HashMap<Locomotive, LocomotiveInfo> locomotivesToConsiderInRouting;
    private final Graph graph;
    private final HashMap<Locomotive, Route> routes;
    private boolean considerElectrification;
    private boolean considerHeight;
    private RoutingAlgorithm algorithm;


    public RouteGeneratorForJGraphT(Graph graph) {
        this.locomotivesToConsiderInRouting = new HashMap<>();
        this.routes = new HashMap<>();
        this.considerElectrification = false;
        this.considerHeight = false;
        this.graph = graph;
        this.algorithm = RoutingAlgorithm.DIJKSTRA;
    }

    @Override
    public RouteGenerator addLocomotive(final Locomotive loc) {
        this.locomotivesToConsiderInRouting.put(loc, new LocomotiveInfo(loc));
        return this;
    }

    @Override
    public RouteGenerator setDestinationForLoc(final Locomotive loc, final GraphPoint destination) {
        this.locomotivesToConsiderInRouting.get(loc).setDestination(destination);
        return this;
    }

    @Override
    public RouteGenerator setFacingDirectionForLoc(final Locomotive loc, final GraphPoint facingDirection) {
        this.locomotivesToConsiderInRouting.get(loc).getLoc().setCurrentFacingDirection(facingDirection);
        return this;
    }

    @Override
    public RouteGenerator considerElectrification(boolean toConsider) {
        this.considerElectrification = true;
        return this;
    }

    @Override
    public RouteGenerator considerHeight(boolean toConsider) {
        this.considerHeight = true;
        return this;
    }

    @Override
    public RouteGenerator setRouteOptimization(final Locomotive loc, final RoutingOptimization optimization) {
        this.locomotivesToConsiderInRouting.get(loc).setOptimisation(optimization);
        return this;
    }

    @Override
    public void generateRoute() throws PathNotPossibleException {
        if (getAmountOfLocomotivesToConsider() == 0) {
            throw new IllegalStateException("No locomotives to consider in routing.");
        } else if (getAmountOfLocomotivesToConsider() > 1) {
            throw new UnsupportedOperationException("Routing of multiple locomotives is not implemented yet.");
        } else {
            Locomotive locomotive = locomotivesToConsiderInRouting.keySet().iterator().next();
            org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> routingGraph = mapGraphToRoutingGraph();

            MonoTrainRoutingWrapper monoTrainRoutingWrapper = new MonoTrainRoutingWrapper(algorithm, routingGraph);
            Route route = monoTrainRoutingWrapper.generateRoute(
                    locomotive,
                    locomotivesToConsiderInRouting.get(locomotive).getDestination(),
                    locomotivesToConsiderInRouting.get(locomotive).getOptimisation()
            );
            routes.put(locomotive, route);

        }
        // TODO: Implement routing
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

    private org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> mapGraphToRoutingGraph() {
        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper(); // TODO consider height and electrification
        return mapper.mapGraphToJGraphT(graph);
    }

    private long getAmountOfLocomotivesToConsider() {
        return this.locomotivesToConsiderInRouting.values().stream().filter(loc -> loc.getDestination() != null).count();
    }

    public RouteGenerator setRoutingAlgorithm(final RoutingAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

}
