package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.Speed;

public class LocSpeedAction extends RoutingAction{
    private final Speed locSpeed;

    public LocSpeedAction(Speed locSpeed) {
        this.locSpeed = locSpeed;
    }

    @Override
    public void performAction(Locomotive loc) {
        loc.setCurrentSpeed(locSpeed);
    }
}
