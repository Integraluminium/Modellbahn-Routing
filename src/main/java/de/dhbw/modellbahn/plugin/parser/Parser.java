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
import java.util.Scanner;


@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class Parser {


    private final Map<LocId, Locomotive> locomotives;
    private final Graph graph;
    private final LocomotiveRepository locomotiveRepository;
    private final SystemCalls systemCalls;

    private RouteBuilder routeBuilder;


    public Parser(Graph graph, LocomotiveRepository locomotiveRepository, SystemCalls systemCalls) {
        this.graph = graph;
        this.locomotiveRepository = locomotiveRepository;
        this.systemCalls = systemCalls;
        locomotives = new HashMap<>();

        routeBuilder = new RouteBuilderForJGraphT(graph);
    }

    private LocId parseLocId(final String locIdString) {
        int locIdInt = Integer.parseInt(locIdString);
        LocId locId = new LocId(locIdInt);
        if (!locomotiveRepository.existsLocomotive(locId)) {
            throw new IllegalArgumentException("Locomotive with id " + locId.id() + " is unknown.");
        }
        ;
        return locId;
    }

    private GraphPoint parseGraphPoint(final String graphPointString) {
        GraphPoint graphPoint = GraphPoint.of(graphPointString);
        if (!graph.contains(graphPoint)) {
            throw new IllegalArgumentException("GraphPoint " + graphPointString + " is not in the graph.");
        }
        return graphPoint;
    }

    private RoutingOptimization parseOptimization(final String optimizationString) {
        try {
            return RoutingOptimization.valueOf(optimizationString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Optimization " + optimizationString + " is unknown.");
        }
    }


    public void runCLI() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("Enter command (type 'help' for options): ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            switch (parts[0]) {
                case "help":
                    printHelp();
                    break;
                case "add":
                    if (parts.length < 2) {
                        System.out.println("Usage: add <loc_id>");
                        break;
                    }
                    addLocomotiveToConsider(parseLocId(parts[1]));
                    break;
                case "destination":
                    if (parts.length < 3) {
                        System.out.println("Usage: destination <loc_id> <destination>");
                        break;
                    }
                    setDestination(parseLocId(parts[1]), parseGraphPoint(parts[2]));
                    break;
                case "facing":
                    if (parts.length < 3) {
                        System.out.println("Usage: facing <loc_id> <facing_direction>");
                        break;
                    }
                    setFacingDirection(parseLocId(parts[1]), parseGraphPoint(parts[2]));
                    break;
                case "electrification":
                    if (parts.length < 2) {
                        System.out.println("Usage: electrification <true|false>");
                        break;
                    }
                    setElectrificationConsideration(Boolean.parseBoolean(parts[1]));
                    break;
                case "height":
                    if (parts.length < 2) {
                        System.out.println("Usage: height <true|false>");
                        break;
                    }
                    setHeightConsideration(Boolean.parseBoolean(parts[1]));
                    break;
                case "optimization":
                    if (parts.length < 3) {
                        System.out.println("Usage: optimization <loc_id> <optimization>");
                        break;
                    }
                    setOptimization(parseLocId(parts[1]), parseOptimization(parts[2]));
                    break;
                case "drive":
                    generateAndDriveRoute();
                    break;
                case "exit":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for options.");
                    break;
            }
        }

        System.out.println("Exiting...");
    }

    private void generateAndDriveRoute() {
        try {
            routeBuilder.generateRoute();
        } catch (PathNotPossibleException e) {
            System.out.println("No route possible." + e);
        }
        System.out.println("Driving route...");
        System.out.println("-------------");
        routeBuilder = new RouteBuilderForJGraphT(graph);
    }

    private void setHeightConsideration(final boolean parsedBoolean) {
        routeBuilder.considerHeight(parsedBoolean);
    }

    private void setElectrificationConsideration(final boolean flag) {
        routeBuilder.considerElectrification(flag);
    }

    private void setFacingDirection(final LocId locId, final GraphPoint direction) {
        routeBuilder.setFacingDirectionForLoc(locomotives.get(locId), direction);

    }

    private void setOptimization(final LocId locId, final RoutingOptimization optimization) {
        routeBuilder.setRouteOptimization(locomotives.get(locId), optimization);
    }

    private void printHelp() {
        System.out.println("Available commands:");
        System.out.println("add <locomotive_name> - Add a locomotive to the route");
        System.out.println("destination <locomotive_name> <destination> - Set the destination for a locomotive");
        System.out.println("facing <locomotive_name> <facing_direction> - Set the facing direction for a locomotive");
        System.out.println("electrification <true|false> - Consider electrification in the route");
        System.out.println("height <true|false> - Consider height in the route");
        System.out.println("optimization <locomotive_name> <optimization> - Set the route optimization for a locomotive");
        System.out.println("drive - Drive the route");
        System.out.println("exit - Exit the program");
    }

    private void addLocomotiveToConsider(LocId locId) {
        Locomotive loc = locomotiveRepository.getLocomotive(locId);
        locomotives.put(locId, loc);
        routeBuilder.addLocomotive(loc);
        System.out.println("Added locomotive: " + loc.getName().name());
    }

    private void setDestination(LocId locId, GraphPoint destination) {
        if (locomotiveRepository.existsLocomotive(locId)) { // Optional Behavior
            addLocomotiveToConsider(locId);
        }

        Locomotive loc = locomotives.get(locId);
        routeBuilder.setDestinationForLoc(loc, destination);
        System.out.println("Set destination for locomotive " + loc.getName().name() + " to " + destination.getName().name());

    }
}
