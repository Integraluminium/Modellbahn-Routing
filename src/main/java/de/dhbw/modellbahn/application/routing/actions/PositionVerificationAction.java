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
    private boolean isSensitive = false;

    private CompletableFuture<Boolean> verificationResult;

    public PositionVerificationAction(DomainEventPublisher eventPublisher, Locomotive locomotive, TrackContact expectedPoint) {
        this(eventPublisher, locomotive, expectedPoint, 10);
    }

    public PositionVerificationAction(DomainEventPublisher eventPublisher, Locomotive locomotive, TrackContact expectedPoint, int timeoutSeconds) {
        this.locomotive = locomotive;
        this.expectedPoint = expectedPoint;
        this.timeoutSeconds = timeoutSeconds;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void performAction() {
        isSensitive = true;
        logger.info("Verifying position of locomotive " + locomotive.getLocId() +
                " expected at " + expectedPoint.getName());

        verificationResult = new CompletableFuture<>();

        // Subscribe to track contact events
        Consumer<TrackContactEvent> contactListener = this::handleContactEvent;
        eventPublisher.subscribe(TrackContactEvent.class, contactListener);

        try {
            Boolean result = verificationResult.get(timeoutSeconds, TimeUnit.SECONDS);
            if (!result) logger.warning("Position verification failed for locomotive " + locomotive.getLocId());

            logger.finer("Successfully verified Position for locomotive " + locomotive.getLocId());

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