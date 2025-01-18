package de.dhbw.modellbahn.application.port.moba.communication;

import de.dhbw.modellbahn.adapter.moba.communication.TrackComponentStatus;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;

public interface TrackComponentCalls {
    void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentStatus trackComponentStatus);

    TrackComponentStatus getTrackComponentStatus(TrackComponentId trackComponentId);

}
