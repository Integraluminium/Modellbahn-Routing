package de.dhbw.modellbahn.plugin.routing.jgrapht.algorithm;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.Map;

public interface TrainRoutingStrategy {
    /**
     * Generates routes for all locomotives in the locomotiveInfoMap according to the strategy
     *
     * @param routingContext@return a map of locomotives to their routes
     * @throws PathNotPossibleException if no path is found
     */
    Map<Locomotive, Route> generateRoutes(final RoutingContext routingContext) throws PathNotPossibleException;
}
