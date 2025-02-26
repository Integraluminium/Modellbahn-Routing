package de.dhbw.modellbahn.application.routing;

public class WaitAction extends RoutingAction {
    private final long waitTime;

    public WaitAction(long waitTime) {
        if (waitTime < 0) {
            throw new IllegalArgumentException("WaitTime must have a positive value.");
        }
        this.waitTime = waitTime;
    }

    @Override
    public void performAction() {
        if (this.waitTime == 0) {
            return;
        }
        try {
            Thread.sleep(this.waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException("Waiting in route failed. \n" + e);
        }
    }

    public long getWaitTime() {
        return waitTime;
    }

    @Override
    public String toString() {
        return "WaitAction{" +
                "waitTime=" + waitTime +
                "ms}";
    }
}
