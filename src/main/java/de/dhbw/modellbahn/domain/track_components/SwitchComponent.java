package de.dhbw.modellbahn.domain.track_components;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;

public class SwitchComponent extends TrackComponent {
    private final TrackComponentCalls trackComponentCalls;
    private SwitchState state;

    public SwitchComponent(TrackComponentId id, SwitchState state, TrackComponentCalls trackComponentCalls) {
        super(id);
        this.state = state;
        this.trackComponentCalls = trackComponentCalls;
    }

    public SwitchComponent(TrackComponentId id, TrackComponentCalls apiAdapter) {
        this(id, SwitchState.UNKNOWN, apiAdapter);

        this.state = synchroniseState();
    }

    public SwitchState synchroniseState() {
        // TODO request API / Synchronise
        return state;
    }

    public SwitchState getState() {
        return state;
    }

    public void setState(SwitchState state) {
        this.trackComponentCalls.setSwitchComponentStatus(this.getId(), state);
        this.state = state;
    }

    public void setStraight() {
        setState(SwitchState.STRAIGHT);
    }

    public void setDiverging() {
        setState(SwitchState.DIVERGENT);
    }
}
