package de.dhbw.modellbahn.domain.routing;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public abstract class RoutingAction {
    public abstract void performAction(Locomotive loc);
}
