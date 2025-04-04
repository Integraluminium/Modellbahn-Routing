package de.dhbw.modellbahn.domain.events;

import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;

public record TrackContactEvent(TrackComponentId contactId, boolean activated) implements DomainEvent {
}