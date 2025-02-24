package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;

public class WaitAction extends RoutingAction{
    private final int waitTime;

    public WaitAction(int waitTime) {
        if (waitTime < 0){
            throw new IllegalArgumentException("WaitTime must have a positive value.");
        }
        this.waitTime = waitTime;
    }

    @Override
    public void performAction(Locomotive loc) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("Waiting in route failed. \n" + e);
        }
    }
}
