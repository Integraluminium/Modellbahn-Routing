package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.RoutingOptimisations;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

class LocomotiveInfo {
    private Locomotive loc;
    private GraphPoint destination;
    private RoutingOptimisations optimisation;

    public LocomotiveInfo(final Locomotive loc) {
        this.loc = loc;
        this.destination = null;
        this.optimisation = RoutingOptimisations.DISTANCE;
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

    public RoutingOptimisations getOptimisation() {
        return optimisation;
    }

    public void setOptimisation(final RoutingOptimisations optimisation) {
        this.optimisation = optimisation;
    }
}
