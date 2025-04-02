package de.dhbw.modellbahn.application.routing.actions;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PositionVerificationAction implements RoutingAction {
    private static final Logger logger = Logger.getLogger(PositionVerificationAction.class.getSimpleName());
    private final Locomotive locomotive;
    private final GraphPoint expectedPoint;
    private final int timeoutSeconds = 10;

    private CompletableFuture<Boolean> verificationResult;

    public PositionVerificationAction(Locomotive locomotive, GraphPoint expectedPoint) {
        this.locomotive = locomotive;
        this.expectedPoint = expectedPoint;
    }

    @Override
    public void performAction() {
        logger.info("Verifying position of locomotive " + locomotive.getLocId() +
                " expected at " + expectedPoint.getName());

        verificationResult = new CompletableFuture<>();

        // Here you would register a listener for the expected contact point
        // When the contact is triggered, the CompletableFuture would be completed

        try {
            Boolean result = verificationResult.get(timeoutSeconds, TimeUnit.SECONDS);
            if (!result) {
                logger.warning("Position verification failed for locomotive " +
                        locomotive.getLocId());
                // Handle verification failure - emergency stop?
                locomotive.emergencyStop();
            }
        } catch (Exception e) {
            logger.severe("Position verification timed out: " + e.getMessage());
            locomotive.emergencyStop();
        }
    }

    public void contactDetected(GraphPoint detectedPoint) {
        if (expectedPoint.equals(detectedPoint)) {
            verificationResult.complete(true);
        } else {
            logger.warning("Unexpected position detected: " + detectedPoint.getName() +
                    " but expected " + expectedPoint.getName());
            verificationResult.complete(false);
        }
    }

    @Override
    public String toString() {
        return "PositionVerificationAction{" +
                "locomotive=" + locomotive.getLocId() +
                ", expectedPoint=" + expectedPoint.getName() +
                '}';
    }
}