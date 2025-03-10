package de.dhbw.modellbahn.application.routing.action;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.Speed;

public class ActionFactory {
    public static LocSpeedAction createLocSpeedAction(Locomotive loc, int speedValue) {
        Speed speed = new Speed(speedValue);
        return new LocSpeedAction(loc, speed);
    }
}
