package de.dhbw.modellbahn.application;

import de.dhbw.modellbahn.application.routing.PathNotPossibleException;
import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public interface RouteBuilder {
    /**
     * Adds a locomotive to the route builder
     * Routing this locomotive is <b>optional</b>
     *
     * @param loc the locomotive to add
     * @return this
     */
    RouteBuilder addLocomotive(Locomotive loc);

    /**
     * Sets the destination for a locomotive
     * If destination is not set, the locomotive will not be routed
     *
     * @param loc         the locomotive
     * @param destination the destination graph point
     * @return this
     */
    RouteBuilder setDestinationForLoc(Locomotive loc, GraphPoint destination);

    /**
     * Sets the facing direction for a locomotive at the destination
     *
     * @param loc             the locomotive to set the facing direction for
     * @param facingDirection neighbor graph point to the destination where the locomotive should face
     * @return this
     */
    RouteBuilder setLocFacingDirectionForDestination(Locomotive loc, GraphPoint facingDirection);

    /**
     * Flags the route builder to consider electrification for every route
     *
     * @param toConsider whether to consider electrification
     * @return this
     */
    RouteBuilder considerElectrification(boolean toConsider);

    /**
     * Flags the route builder to consider height as weight for every route
     *
     * @param toConsider whether to consider height
     * @return this
     */
    RouteBuilder considerHeight(boolean toConsider);

    /**
     * Set optimization parameters for the routing of one locomotive
     * <i>Optional</i>
     * <b>only one optimization can be set per locomotive
     *
     * @param loc
     * @param optimization
     * @return
     */
    RouteBuilder setRouteOptimization(Locomotive loc, RoutingOptimization optimization);

    /**
     * Generates the route for all locomotives
     * <b>Is finishing the configuration</b>
     * Individually generated routes can be accessed with {@link #getRouteForLoc(Locomotive)}
     *
     * @throws PathNotPossibleException if no route is possible
     */
    void generateRoute() throws PathNotPossibleException;

    /**
     * Gets the route for a locomotive <b>after</b> {@link #generateRoute()} has been called
     *
     * @param loc the locomotive
     * @return the route
     * @throws IllegalStateException if {@link #generateRoute()} has not been called
     */
    Route getRouteForLoc(Locomotive loc);

    /**
     * Return all locomotives which are used in routing
     *
     * @return locomotives
     */
    Iterable<Locomotive> getLocomotivesWithRoute();
}
