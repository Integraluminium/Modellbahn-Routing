package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.events.DomainEventPublisher;
import de.dhbw.modellbahn.domain.events.TrackContactEvent;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.TrackContact;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AwaitTrackContact implements Instruction {
    private static final Logger logger = Logger.getLogger(AwaitTrackContact.class.getName());

    private final TrackContact expectedPoint;
    private final int timeoutSeconds;
    private final CompletableFuture<Boolean> verificationResult;
    private final DomainEventPublisher eventPublisher;
    private volatile boolean isSensitive = true;

    public AwaitTrackContact(TrackContact expectedPoint, int timeoutSeconds) {
        this.expectedPoint = expectedPoint;
        this.timeoutSeconds = timeoutSeconds;
        this.verificationResult = new CompletableFuture<>();
        this.eventPublisher = DomainEventPublisher.getInstance();
    }

    @Override
    public void execute(final CommandContext context) throws InstructionException {
        // Subscribe to track contact events
        Consumer<TrackContactEvent> contactListener = this::handleContactEvent;
        eventPublisher.subscribe(TrackContactEvent.class, contactListener);

        try {
            Boolean result = verificationResult.get(timeoutSeconds, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(result)) {
                throw new InstructionException("Track contact verification failed");
            }
            logger.fine("Successfully verified track contact: " + expectedPoint.getName());
        } catch (Exception e) {
            // Timeout or other error
            throw new InstructionException("Timed out waiting for track contact: " + expectedPoint.getName(), e);
        } finally {
            isSensitive = false;
            eventPublisher.unsubscribe(TrackContactEvent.class, contactListener);
        }
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("AWAIT TRACK_CONTACT " + expectedPoint.getName() + " TIMEOUT " + timeoutSeconds);
    }

    private void handleContactEvent(TrackContactEvent event) {
        if (!isSensitive) return;

        // Extract the component ID from the TrackContact and compare with the event's contactId
        if (event.activated() && expectedPoint.hasTrackComponentId(event.contactId())) {
            verificationResult.complete(true);
        }
    }
}