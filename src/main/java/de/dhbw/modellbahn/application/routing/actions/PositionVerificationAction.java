package de.dhbw.modellbahn.application.routing.actions;

import de.dhbw.modellbahn.domain.events.DomainEventPublisher;
import de.dhbw.modellbahn.domain.events.TrackContactEvent;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.TrackContact;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class PositionVerificationAction implements RoutingAction {
    private static final Logger logger = Logger.getLogger(PositionVerificationAction.class.getSimpleName());
    private final Locomotive locomotive;
    private final TrackContact expectedPoint;
    private final int timeoutSeconds;
    private final DomainEventPublisher eventPublisher;
    private final long negSensitivityTime;
    private boolean isSensitive = false;
    private CompletableFuture<Boolean> verificationResult;

    public PositionVerificationAction(DomainEventPublisher eventPublisher, Locomotive locomotive, TrackContact expectedPoint) {
        this(eventPublisher, locomotive, expectedPoint, 10, 0);
    }

    public PositionVerificationAction(DomainEventPublisher eventPublisher, Locomotive locomotive, TrackContact expectedPoint, int timeoutSeconds, long negSensitivityTime) {
        this.locomotive = locomotive;
        this.expectedPoint = expectedPoint;
        this.timeoutSeconds = timeoutSeconds;
        this.eventPublisher = eventPublisher;
        this.negSensitivityTime = negSensitivityTime;
    }

    @Override
    public void performAction() {
        long startTime = System.nanoTime();
        isSensitive = true;
        logger.fine("Verifying position of locomotive " + locomotive.getLocId() +
                " expected at " + expectedPoint.getName());

        verificationResult = new CompletableFuture<>();

        // Subscribe to track contact events
        Consumer<TrackContactEvent> contactListener = this::handleContactEvent;
        eventPublisher.subscribe(TrackContactEvent.class, contactListener);

        try {
            Boolean result = verificationResult.get(timeoutSeconds, TimeUnit.SECONDS);
            long elapsedTimeInMs = (System.nanoTime() - startTime) / 1_000_000;
            if (!result) logger.warning("Position verification failed for locomotive " + locomotive.getLocId());

            logger.info("Successfully verified Position for locomotive " + locomotive.getLocId() + " with " + ((elapsedTimeInMs - negSensitivityTime)) + "ms elapsed compared to allocated Time");

        } catch (Exception e) {
            logger.severe("Position verification timed out: " + e.getMessage());
            locomotive.emergencyStop();
        } finally {
            isSensitive = false;
            eventPublisher.unsubscribe(TrackContactEvent.class, contactListener);
        }
    }

    public void handleContactEvent(TrackContactEvent event) {
        if (!isSensitive) return; // Ignore if not actively verifying

        if (!(event.activated() || expectedPoint.hasTrackComponentId(event.contactId()))) {
            return; // Unexpected contact
        }

        logger.fine("Position verified: locomotive " + locomotive.getLocId() + " detected at " + expectedPoint.getName());
        verificationResult.complete(true);
    }

    @Override
    public String toString() {
        return "PositionVerificationAction{" +
                "locomotive=" + locomotive.getLocId() +
                ", expectedPoint=" + expectedPoint.getName() +
                '}';
    }
}