package de.dhbw.modellbahn.application.routing.action;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public final class LocToggleAction extends RoutingAction {
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
