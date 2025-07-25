package de.dhbw.modellbahn.application.routing.actions;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;

public class LocSpeedAction implements RoutingAction {
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

    public Speed getLocSpeed() {
        return locSpeed;
    }

    @Override
    public String toString() {
        return "LocSpeedAction{" +
                "locId=" + loc.getLocId().id() +
                ", locSpeed=" + locSpeed.value() +
                "%}";
    }
}
