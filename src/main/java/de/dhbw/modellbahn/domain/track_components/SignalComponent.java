package de.dhbw.modellbahn.domain.track_components;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;

public class SignalComponent extends TrackComponent {
    private SignalState state;
    private TrackComponentCalls apiAdapter;

    public SignalComponent(TrackComponentId id, TrackComponentCalls apiAdapter) {
        super(id);
        state = SignalState.UNKNOWN;
        this.apiAdapter = apiAdapter;
    }

    public void setState(SignalState state) {
        if (state == SignalState.UNKNOWN) {
            throw new IllegalArgumentException("State should not be set to UNDEFINED");
        }
        this.apiAdapter.setSignalComponentStatus(this.getId(), state);
        this.state = state;
    }

    public void setClear() {
        setState(SignalState.CLEAR);
    }

    public void setDanger() {
        setState(SignalState.DANGER);
    }
}
