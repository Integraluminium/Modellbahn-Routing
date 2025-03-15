package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RouteBuilder;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.RouteBuilderForJGraphT;

import java.util.HashMap;
import java.util.Map;

public class Actions {
    private final Map<LocId, Locomotive> locomotives;
    private final SystemCalls systemCalls;
    private final Graph graph;
    private final LocomotiveRepository locomotiveRepository;
    private RouteBuilder routeBuilder;

    public Actions(final Graph graph, final LocomotiveRepository locomotiveRepository, final SystemCalls systemCalls) {
        this.systemCalls = systemCalls;
        this.graph = graph;
        this.locomotiveRepository = locomotiveRepository;

        this.locomotives = new HashMap<>();
        routeBuilder = new RouteBuilderForJGraphT(graph);
    }

    public void generateAndDriveRoute() {
        try {
            routeBuilder.generateRoute();
        } catch (PathNotPossibleException e) {
            System.out.println("No route possible." + e);
        }
        System.out.println("Driving route...");
        System.out.println("-------------");
        routeBuilder = new RouteBuilderForJGraphT(graph);
    }

    public void setHeightConsideration(final boolean parsedBoolean) {
        routeBuilder.considerHeight(parsedBoolean);
    }

    public void setElectrificationConsideration(final boolean flag) {
        routeBuilder.considerElectrification(flag);
    }

    public void setFacingDirection(final LocId locId, final GraphPoint direction) {
        routeBuilder.setFacingDirectionForLoc(locomotives.get(locId), direction);

    }

    public void setOptimization(final LocId locId, final RoutingOptimization optimization) {
        routeBuilder.setRouteOptimization(locomotives.get(locId), optimization);
    }


    public void addLocomotiveToConsider(LocId locId) {
        Locomotive loc = locomotiveRepository.getLocomotive(locId);
        locomotives.put(locId, loc);
        routeBuilder.addLocomotive(loc);
        System.out.println("Added locomotive: " + loc.getName().name());
    }

    public void setDestination(LocId locId, GraphPoint destination) {
        if (locomotiveRepository.existsLocomotive(locId)) { // Optional Behavior
            addLocomotiveToConsider(locId);
        }

        Locomotive loc = locomotives.get(locId);
        routeBuilder.setDestinationForLoc(loc, destination);
        System.out.println("Set destination for locomotive " + loc.getName().name() + " to " + destination.getName().name());

    }
}
