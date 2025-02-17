package de.dhbw.modellbahn.domain.track_components;

public abstract class TrackComponent {
    private final TrackComponentId id;


    public TrackComponent(TrackComponentId id) {
        this.id = id;
    }

    public TrackComponentId getId() {
        return id;
    }

}
