package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

class LocomotiveInfo {
    private Locomotive loc;
    private GraphPoint destination;
    private RoutingOptimization optimisation;

    public LocomotiveInfo(final Locomotive loc) {
        this.loc = loc;
        this.destination = loc.getCurrentPosition();
        this.optimisation = RoutingOptimization.DISTANCE;
    }

    public Locomotive getLoc() {
        return loc;
    }

    public void setLoc(final Locomotive loc) {
        this.loc = loc;
    }

    public GraphPoint getDestination() {
        return destination;
    }

    public void setDestination(final GraphPoint destination) {
        this.destination = destination;
    }

    public RoutingOptimization getOptimisation() {
        return optimisation;
    }

    public void setOptimisation(final RoutingOptimization optimisation) {
        this.optimisation = optimisation;
    }
}
