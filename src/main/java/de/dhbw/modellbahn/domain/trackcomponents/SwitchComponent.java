package de.dhbw.modellbahn.domain.trackcomponents;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.adapter.moba.communication.TrackComponentStatus;

public class SwitchComponent extends TrackComponent {
    private final TrackComponentCalls trackComponentCalls;
    private SwitchState state;

    public SwitchComponent(String name, TrackComponentId id, SwitchState state, TrackComponentCalls trackComponentCalls) {
        super(name, id);
        this.state = state;
        this.trackComponentCalls = trackComponentCalls;
    }

    public SwitchComponent(String name, TrackComponentId id, TrackComponentCalls apiAdapter) {
        this(name, id, SwitchState.UNKNOWN, apiAdapter);

        this.state = synchroniseState();
    }

    public SwitchState synchroniseState() {
        // TODO request API / Synchronise
        return state;
    }

    public SwitchState getState() {
        return state;
    }

    public void setState(SwitchState state, TrackComponentStatus status) {
        this.trackComponentCalls.setTrackComponentStatus(this.getId(), status);
        this.state = state;
    }

    public void setStraight() {
        setState(SwitchState.STRAIGHT, new TrackComponentStatus(1));
    }

    public void setDiverging() {
        setState(SwitchState.DIVERGENT, new TrackComponentStatus(0));
    }
}
