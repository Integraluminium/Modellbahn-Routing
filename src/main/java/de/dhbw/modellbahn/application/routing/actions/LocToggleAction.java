package de.dhbw.modellbahn.application.routing.actions;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public final class LocToggleAction implements RoutingAction {
    private final Locomotive locomotive;
    private final GraphPoint facingDirection;

    public LocToggleAction(Locomotive locomotive, GraphPoint facingDirection) {
        this.locomotive = locomotive;
        this.facingDirection = facingDirection;
    }

    @Override
    public String toString() {
        return "LocToggleAction[" +
                "locomotive=" + locomotive.getLocId() + ", " +
                "point=" + facingDirection + ']';
    }

    @Override
    public void performAction() {
        locomotive.toggleLocomotiveDirection(facingDirection);
    }
}
