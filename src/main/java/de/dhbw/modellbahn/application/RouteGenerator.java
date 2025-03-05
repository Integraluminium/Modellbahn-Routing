package de.dhbw.modellbahn.application;

import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public interface RouteGenerator {
    RouteGenerator addLocomotive(Locomotive loc);

    RouteGenerator setDestinationForLoc(Locomotive locomotive, GraphPoint destination);

    RouteGenerator considerElectrification(boolean toConsider);

    RouteGenerator considerHeight(boolean toConsider);

    RouteGenerator setRouteOptimisation(Locomotive loc, RoutingOptimisations optimisation);

    void generateRoute() throws PathNotPossibleException;

    Route getRouteForLoc(Locomotive loc);
}
