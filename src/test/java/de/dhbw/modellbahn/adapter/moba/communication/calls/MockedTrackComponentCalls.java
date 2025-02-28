package de.dhbw.modellbahn.adapter.moba.communication.calls;


import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.track_components.SignalState;
import de.dhbw.modellbahn.domain.track_components.SwitchState;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;

public class MockedTrackComponentCalls implements TrackComponentCalls {

    @Override
    public void setSwitchComponentStatus(TrackComponentId trackComponentId, SwitchState switchState) {

    }

    @Override
    public SwitchState getSwitchComponentStatus(TrackComponentId trackComponentId) {
        return SwitchState.UNKNOWN;
    }

    @Override
    public void setSignalComponentStatus(TrackComponentId trackComponentId, SignalState signalState) {

    }

    @Override
    public SwitchState getSignalComponentStatus(TrackComponentId trackComponentId) {
        return SwitchState.UNKNOWN;
    }
}