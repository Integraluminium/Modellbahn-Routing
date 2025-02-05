package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.TrackSensor;

public class TrackContact extends GraphPoint {
    private final TrackSensor trackSensor;

    public TrackContact(String name, TrackSensor trackSensor) {
        super(name);
        this.trackSensor = trackSensor;
    }
}
