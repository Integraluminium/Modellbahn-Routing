package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.events.DomainEventPublisher;
import de.dhbw.modellbahn.domain.events.TrackContactEvent;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.TrackContact;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AwaitTrackContact implements Instruction {
    private static final Logger logger = Logger.getLogger(AwaitTrackContact.class.getName());

    private final TrackContact expectedPoint;
    private final int milliSeconds;
    private final CompletableFuture<Boolean> verificationResult;
    private final DomainEventPublisher eventPublisher;

    public AwaitTrackContact(TrackContact expectedPoint, int milliSeconds) {
        this.expectedPoint = expectedPoint;
        this.milliSeconds = milliSeconds;
        this.verificationResult = new CompletableFuture<>();
        this.eventPublisher = DomainEventPublisher.getInstance();
    }


    @Override
    public void execute(final CommandContext context) throws InstructionException {
        logger.info("Awaiting track contact: " + expectedPoint.getName() + " with timeout: " + milliSeconds + " seconds");

        // Subscribe to track contact events
        Consumer<TrackContactEvent> contactListener = this::handleContactEvent;
        eventPublisher.subscribe(TrackContactEvent.class, contactListener);

        try {
            // Busy waiting implementation
            final long startTime = System.currentTimeMillis();

            while (true) {
                // Process any pending events by yielding this thread briefly
                Thread.yield();

                // Check if we've received the event
                if (verificationResult.isDone()) {
                    try {
                        boolean result = verificationResult.getNow(false);
                        if (!result) {
                            throw new InstructionException("Track contact verification failed");
                        }

                        logger.info("Successfully verified track contact: " + expectedPoint.getName());
                        return;
                    } catch (Exception e) {
                        if (!(e instanceof InstructionException)) {
                            throw new InstructionException("Error during verification", e);
                        }
                        throw (InstructionException) e;
                    }
                }

                // Check if we've timed out
                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > milliSeconds) {
                    logger.warning("Timed out waiting for track contact: " + expectedPoint.getName());
                    context.getOutput().println("Timed out waiting for track contact: " + expectedPoint.getName());
                    return; // Return without exception
                }

                // Sleep briefly to avoid excessive CPU usage
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new InstructionException("Track contact verification interrupted", e);
                }
            }
        } finally {

            eventPublisher.unsubscribe(TrackContactEvent.class, contactListener);
        }
    }


    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("AWAIT TRACK_CONTACT " + expectedPoint.getName() + " TIMEOUT " + milliSeconds);
    }

    private void handleContactEvent(TrackContactEvent event) {


        // Extract the component ID from the TrackContact and compare with the event's contactId
        if (event.activated() && expectedPoint.hasTrackComponentId(event.contactId())) {
            verificationResult.complete(true);
        }
    }
}