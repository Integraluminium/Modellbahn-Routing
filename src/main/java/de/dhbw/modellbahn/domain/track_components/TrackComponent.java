package de.dhbw.modellbahn.domain.track_components;

public abstract class TrackComponent {
    private final String name;
    private final TrackComponentId id;


    public TrackComponent(String name, TrackComponentId id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public TrackComponentId getId() {
        return id;
    }

}
