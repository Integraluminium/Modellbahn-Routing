package de.dhbw.modellbahn.application.routing;

public class WaitAction extends RoutingAction {
    private final long waitTime;

    public WaitAction(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void performAction() {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("Waiting in route failed. \n" + e);
        }
    }
}
