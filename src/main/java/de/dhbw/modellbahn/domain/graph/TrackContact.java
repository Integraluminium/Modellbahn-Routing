package de.dhbw.modellbahn.domain.graph;

import de.dhbw.modellbahn.domain.track_components.TrackContactComponent;

public class TrackContact extends GraphPoint {
    private final TrackContactComponent trackContactComponent;

    public TrackContact(PointName name, TrackContactComponent trackContactComponent) {
        super(name);
        this.trackContactComponent = trackContactComponent;
    }
}
