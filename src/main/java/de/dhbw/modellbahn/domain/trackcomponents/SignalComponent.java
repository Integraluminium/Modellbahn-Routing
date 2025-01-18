package de.dhbw.modellbahn.domain.trackcomponents;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.adapter.moba.communication.TrackComponentStatus;

public class SignalComponent extends TrackComponent {
    private SignalState state;
    private TrackComponentCalls apiAdapter;

    public SignalComponent(String name, TrackComponentId id, TrackComponentCalls apiAdapter) {
        super(name, id);
        state = SignalState.UNKNOWN;
        this.apiAdapter = apiAdapter;
    }

    public void setState(SignalState state, TrackComponentStatus status) {
        if (state == SignalState.UNKNOWN) {
            throw new IllegalArgumentException("State should not be set to UNDEFINED");
        }
        this.apiAdapter.setTrackComponentStatus(this.getId(), status);
        this.state = state;
    }

    public void setClear() {
        setState(SignalState.CLEAR, new TrackComponentStatus(1));
    }

    public void setDanger() {
        setState(SignalState.DANGER, new TrackComponentStatus(0));
    }
}
