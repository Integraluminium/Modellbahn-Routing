package de.dhbw.modellbahn.plugin.parser.lexer;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RouteBuilder;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.RouteBuilderForJGraphT;

import java.io.PrintStream;
import java.util.List;

public class CommandContext {
    private final LocomotiveRepository locomotiveRepository;
    private final Graph graph;
    private final PrintStream output;
    private final SystemCalls systemCalls;

    private RouteBuilder routeBuilder;

    public CommandContext(final LocomotiveRepository locomotiveRepository, final Graph graph, final SystemCalls systemCalls, final PrintStream output) {
        this.locomotiveRepository = locomotiveRepository;
        this.graph = graph;
        this.output = output;
        this.systemCalls = systemCalls;
        resetRouteBuilder();
    }

    public SystemCalls getSystemCalls() {
        return systemCalls;
    }

    public Locomotive getLocomotive(final LocId locId) {
        return locomotiveRepository.getLocomotive(locId);
    }

    public RouteBuilder getCurrentRouteBuilder() {
        return routeBuilder;
    }

    public void resetRouteBuilder() {
        routeBuilder = new RouteBuilderForJGraphT(graph);
    }

    public PrintStream getOutput() {
        return output;
    }

    public boolean existsLocomotive(final LocId locId) {
        return locomotiveRepository.existsLocomotive(locId);
    }

    public Iterable<LocId> getLocInfos() {
        return locomotiveRepository.getAvailableLocIds();
    }

    public Iterable<GraphPoint> getGraphPoints() {
        return graph.getAllVertices();
    }

    public void generateRoute() throws PathNotPossibleException {
        routeBuilder.generateRoute();
    }

    public void toggleLocomotiveDirection(LocId locId) {
        // TODO FIX ISSUES WITH SWITCHES - NOT WORKING PROPERLY

        Locomotive loc = locomotiveRepository.getLocomotive(locId);
        GraphPoint current = loc.getCurrentFacingDirection();
        GraphPoint position = loc.getCurrentPosition();

        // Find a neighbor that is not the current position
        List<GraphPoint> neighbors = graph.getNeighbors(position);
        if (neighbors == null || neighbors.isEmpty() || neighbors.size() == 1) {
            output.println("Could not toggle direction, no neighbors or only one neighbor found");
            return;
        }
        GraphPoint otherConnectedPoint = null;
        if (current instanceof Switch currentSwitch) {
            otherConnectedPoint = currentSwitch.getPointThatCanConnectThisPoint(current);
        } else {
            for (GraphPoint connected : neighbors) {
                if (!connected.equals(current)) {
                    otherConnectedPoint = connected;
                    break;
                }
            }
        }
        if (otherConnectedPoint == null) {
            output.println("Could not toggle direction, no alternative found");
            return;
        }

        loc.toggleLocomotiveDirection(otherConnectedPoint);
    }

    public void listAllGraphPoints() {
        output.println("All graph points:");
        getGraphPoints().forEach(point ->
                output.println(" - " + point.getName().name() +
                        " connects to: " + graph.getNeighbors(point)));
    }

    public void driveRoute() {
        routeBuilder.getLocomotivesWithRoute().forEach(locomotive -> {
            Route route = routeBuilder.getRouteForLoc(locomotive);
            route.driveRoute();
        });
        resetRouteBuilder();
    }
}
