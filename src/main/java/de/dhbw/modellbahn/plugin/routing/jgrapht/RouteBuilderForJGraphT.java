package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RouteBuilder;
import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm.MonoTrainRouting;
import de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm.RoutingContext;
import de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm.SerializedPolyTrainRouting;
import de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm.TrainRoutingStrategy;

import java.util.HashMap;
import java.util.Map;

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
        this.locomotivesToConsiderInRouting.get(loc).setOptimization(optimization);
        return this;
    }

    @Override
    public void generateRoute() throws PathNotPossibleException {
        if (getAmountOfLocomotivesToConsider() == 0) {
            throw new IllegalStateException("No locomotives to consider in routing.");
        }
        TrainRoutingStrategy routingAlgorithm;
        if (getAmountOfLocomotivesToConsider() > 1) {
            routingAlgorithm = new SerializedPolyTrainRouting();
        } else {
            routingAlgorithm = new MonoTrainRouting();
        }
        RoutingContext routingContext = new RoutingContext(
                graph,
                locomotivesToConsiderInRouting,
                algorithm,
                considerElectrification,
                considerHeight
        );
        Map<Locomotive, Route> locomotiveRouteMap = routingAlgorithm.generateRoutes(routingContext);
        routes.putAll(locomotiveRouteMap);
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

    @Override
    public RouteBuilder setRoutingAlgorithm(final RoutingAlgorithm algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    private long getAmountOfLocomotivesToConsider() {
        return this.locomotivesToConsiderInRouting.values().stream().filter(locInfo -> locInfo.getDestination() != locInfo.getLoc().getCurrentPosition()).count();
    }
}
