package de.dhbw.modellbahn.adapter.physical.railway.communication.socket;

import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.S88EventCommand;
import de.dhbw.modellbahn.domain.events.DomainEventPublisher;
import de.dhbw.modellbahn.domain.events.TrackContactEvent;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;

import java.util.logging.Logger;

public class TrackContactHandler {
    private static final Logger logger = Logger.getLogger(TrackContactHandler.class.getSimpleName());
    private final DomainEventPublisher eventPublisher;

    public TrackContactHandler(DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void handleEvent(S88EventCommand event) {
        Integer contactId = event.getContactId();
        if (contactId == null) {
            logger.warning("Received S88 event with null contact ID");
            return;
        }

        boolean activated = false;
        if (event.getStateOld() != null && event.getStateNew() != null) {
            if (event.getStateOld() == 0 && event.getStateNew() == 1) {
                activated = true;
                logger.fine("Track contact activated: " + contactId);
            } else if (event.getStateOld() == 1 && event.getStateNew() == 0) {
                logger.fine("Track contact deactivated: " + contactId);
            } else {
                return; // No state change we care about
            }
        }

        eventPublisher.publish(new TrackContactEvent(
                new TrackComponentId(contactId),
                activated
        ));
    }

}