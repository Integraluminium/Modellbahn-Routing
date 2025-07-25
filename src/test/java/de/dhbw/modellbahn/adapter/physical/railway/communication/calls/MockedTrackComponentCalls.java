package de.dhbw.modellbahn.adapter.physical.railway.communication.calls;


import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.physical.railway.components.SignalState;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchState;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;

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