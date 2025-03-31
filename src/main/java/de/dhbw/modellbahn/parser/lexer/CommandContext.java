package de.dhbw.modellbahn.parser.lexer;

import de.dhbw.modellbahn.application.repositories.LocomotiveRepository;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.building.RouteBuilder;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.Switch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.physical.railway.communication.SystemCalls;
import de.dhbw.modellbahn.plugin.routing.jgrapht.RouteBuilderForJGraphT;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class CommandContext {
    private final LocomotiveRepository locomotiveRepository;
    private final Graph graph;
    private final PrintStream output;
    private final SystemCalls systemCalls;

    private boolean automaticallyAddAllLocomotivesToRoute = true;
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

        if (automaticallyAddAllLocomotivesToRoute) {
            locomotiveRepository.getAvailableLocIds().stream()
                    .map(locomotiveRepository::getLocomotive)
                    .filter(this::getNotOnTrack)
                    .forEach(routeBuilder::addLocomotive);
        }
    }

    private boolean getNotOnTrack(Locomotive locomotive) {
        return locomotive.getCurrentPosition().equals(new PointName("NotOnTrack"));
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

    public void setAutomaticallyAddAllLocomotivesToRoute(final boolean automaticallyAddAllLocomotivesToRoute) {
        this.automaticallyAddAllLocomotivesToRoute = automaticallyAddAllLocomotivesToRoute;
    }

    public void toggleLocomotiveDirection(LocId locId) {
        // TODO FIX ISSUES WITH SWITCHES - NOT WORKING PROPERLY

        Locomotive loc = locomotiveRepository.getLocomotive(locId);
        GraphPoint currentFacingDirection = loc.getCurrentFacingDirection();
        GraphPoint position = loc.getCurrentPosition();

        // Find a neighbor that is not the current position
        List<GraphPoint> neighbors = graph.getNeighbors(position);
        if (neighbors == null || neighbors.isEmpty() || neighbors.size() == 1) {
            output.println("Could not toggle direction, no neighbors or only one neighbor found");
            return;
        }
        GraphPoint otherConnectedPoint = null;
        if (currentFacingDirection instanceof Switch currentSwitch) {
            otherConnectedPoint = currentSwitch.getPointThatCanConnectThisPoint(currentFacingDirection);
        } else {
            for (GraphPoint connected : neighbors) {
                if (!connected.equals(currentFacingDirection)) {
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
        List<Thread> threadList = new ArrayList<>();
        routeBuilder.getLocomotivesWithRoute().forEach(locomotive -> {
            Route route = routeBuilder.getRouteForLoc(locomotive);
            String name = "Route-Thread-" + locomotive.getLocId().id();
            threadList.add(new Thread(route::driveRoute, name));
        });
        threadList.forEach(Thread::start);
        threadList.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        resetRouteBuilder();
    }
}
