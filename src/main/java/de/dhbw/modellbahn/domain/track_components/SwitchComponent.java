package de.dhbw.modellbahn.domain.track_components;

public class SwitchComponent extends TrackComponent {
    private TrackComponentState state;

    public SwitchComponent(TrackComponentState state) {
        this.state = state;
    }

    public SwitchComponent() {
        // TODO Frage Status über ID ab
        TrackComponentState actualState = null;
        this.state = actualState;
    }

    public TrackComponentState getState() {
        // TODO request API / Synchronise
        return state;
    }

    public void setState(TrackComponentState state) {
        // TODO request API
        this.state = state;
    }

    public void setStraight(){
        setState(new TrackComponentState(true));
    }
    public void setDiverging(){
        setState(new TrackComponentState(false));
    }
}
