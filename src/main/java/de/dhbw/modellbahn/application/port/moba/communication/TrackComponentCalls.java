package de.dhbw.modellbahn.application.port.moba.communication;

import de.dhbw.modellbahn.domain.track_components.SignalState;
import de.dhbw.modellbahn.domain.track_components.SwitchState;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;

public interface TrackComponentCalls {
//    void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentStatus trackComponentStatus);
//
//    TrackComponentStatus getTrackComponentStatus(TrackComponentId trackComponentId);

    void setSwitchComponentStatus(TrackComponentId trackComponentId, SwitchState switchState);

    SwitchState getSwitchComponentStatus(TrackComponentId trackComponentId);

    void setSignalComponentStatus(TrackComponentId trackComponentId, SignalState signalState);

    SwitchState getSignalComponentStatus(TrackComponentId trackComponentId);
}
