package de.dhbw.modellbahn.domain.physical.railway.communication;

import de.dhbw.modellbahn.domain.physical.railway.components.SignalState;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchState;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;

public interface TrackComponentCalls {
//    void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentStatus trackComponentStatus);
//
//    TrackComponentStatus getTrackComponentStatus(TrackComponentId trackComponentId);

    void setSwitchComponentStatus(TrackComponentId trackComponentId, SwitchState switchState);

    SwitchState getSwitchComponentStatus(TrackComponentId trackComponentId);

    void setSignalComponentStatus(TrackComponentId trackComponentId, SignalState signalState);

    SwitchState getSignalComponentStatus(TrackComponentId trackComponentId);
}
