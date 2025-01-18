package de.dhbw.modellbahn.domain.routing;

import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public interface Routing {
    void buildGraph(Graph graph);
    void addLocToGraph(Locomotive loc);
    Route calculateRoute(Locomotive loc, GraphPoint destination);
}
