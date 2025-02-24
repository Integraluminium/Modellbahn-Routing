package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.Speed;

public class LocSpeedAction extends RoutingAction{
    private final Locomotive loc;
    private final Speed locSpeed;

    public LocSpeedAction(Locomotive loc, Speed locSpeed) {
        this.loc = loc;
        this.locSpeed = locSpeed;
    }

    @Override
    public void performAction() {
        loc.setCurrentSpeed(locSpeed);
    }
}
