package de.dhbw.modellbahn.application;

import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public interface RouteBuilder {
    RouteBuilder addLocomotive(Locomotive loc);

    RouteBuilder setDestinationForLoc(Locomotive loc, GraphPoint destination);

    RouteBuilder setFacingDirectionForLoc(Locomotive loc, GraphPoint facingDirection);

    RouteBuilder considerElectrification(boolean toConsider);

    RouteBuilder considerHeight(boolean toConsider);

    RouteBuilder setRouteOptimization(Locomotive loc, RoutingOptimization optimization);

    void generateRoute() throws PathNotPossibleException;

    Route getRouteForLoc(Locomotive loc);
}
