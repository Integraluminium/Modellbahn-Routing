package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.Objects;

public class WaitAction extends RoutingAction {
    private final Locomotive loc;
    private final Distance distanceTraveled;
    private final Distance pufferDistance;

    public WaitAction(Locomotive loc, Distance distanceTraveled, Distance pufferDistance) {
        Objects.requireNonNull(loc, "Loc must not be null.");
        Objects.requireNonNull(distanceTraveled, "DistanceTraveled must not be null.");
        Objects.requireNonNull(pufferDistance, "PufferDistance must not be null.");
        this.loc = loc;
        this.distanceTraveled = distanceTraveled;
        this.pufferDistance = pufferDistance;
    }

    @Override
    public void performAction() {
        try {
            Thread.sleep(calculateWaitTime(loc.getAccelerationDistance(), loc.getDecelerationDistance()));
        } catch (InterruptedException e) {
            throw new RuntimeException("Waiting in route failed. \n" + e);
        }
    }

    private long calculateWaitTime(Distance accelerationDistance, Distance decelerationDistance) {
        //TODO calculate time
        return 0;
    }
}
