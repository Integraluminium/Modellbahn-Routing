package de.dhbw.modellbahn.plugin.routing.jgrapht;

import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.Optional;

public class LocomotiveInfo {
    private Locomotive loc;
    private GraphPoint destination;
    private GraphPoint destinationFacingDirection;
    private RoutingOptimization optimization;

    public LocomotiveInfo(final Locomotive loc) {
        this.loc = loc;
        this.destination = loc.getCurrentPosition();
        this.optimization = RoutingOptimization.DISTANCE;
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

    public Optional<GraphPoint> getDestinationFacingDirection() {
        return Optional.ofNullable(destinationFacingDirection);
    }

    public void setDestinationFacingDirection(final GraphPoint destinationFacingDirection) {
        // NOTE: There might be a validation if the facing direction is neighboring the destination
        this.destinationFacingDirection = destinationFacingDirection;
    }

    public RoutingOptimization getOptimization() {
        return optimization;
    }

    public void setOptimization(final RoutingOptimization optimization) {
        this.optimization = optimization;
    }
}
