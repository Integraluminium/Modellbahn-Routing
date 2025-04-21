package de.dhbw.modellbahn.application.routing.actions;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.Signal;
import de.dhbw.modellbahn.domain.physical.railway.components.SignalState;

public record SignalChangeAction(
        Signal signalPoint,
        SignalState state
) implements RoutingAction {

    @Override
    public void performAction() {
        signalPoint.setSignalState(state);
    }
}
