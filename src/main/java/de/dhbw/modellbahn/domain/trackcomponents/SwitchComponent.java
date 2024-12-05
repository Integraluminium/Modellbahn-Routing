package de.dhbw.modellbahn.domain.trackcomponents;

import de.dhbw.modellbahn.adapter.api.TrackComponentApiAdapter;
import de.dhbw.modellbahn.adapter.api.TrackComponentStatus;

public class SwitchComponent extends TrackComponent {
    private final TrackComponentApiAdapter trackComponentApiAdapter;
    private SwitchState state;

    public SwitchComponent(String name, TrackComponentId id, SwitchState state, TrackComponentApiAdapter trackComponentApiAdapter) {
        super(name, id);
        this.state = state;
        this.trackComponentApiAdapter = trackComponentApiAdapter;
    }

    public SwitchComponent(String name, TrackComponentId id, TrackComponentApiAdapter apiAdapter) {
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
        this.trackComponentApiAdapter.setTrackComponentStatus(this.getId(), status);
        this.state = state;
    }

    public void setStraight() {
        setState(SwitchState.STRAIGHT, new TrackComponentStatus(1));
    }

    public void setDiverging() {
        setState(SwitchState.DIVERGENT, new TrackComponentStatus(0));
    }
}
