package de.dhbw.modellbahn.plugin.parser.lexer;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RouteBuilder;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.routing.jgrapht.RouteBuilderForJGraphT;

import java.io.PrintStream;

public class CommandContext {
    private final LocomotiveRepository locomotiveRepository;
    private final Graph graph;
    private final PrintStream output;

    private RouteBuilder routeBuilder;

    public CommandContext(final LocomotiveRepository locomotiveRepository, final Graph graph, final PrintStream output) {
        this.locomotiveRepository = locomotiveRepository;
        this.graph = graph;
        this.output = output;
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
}
