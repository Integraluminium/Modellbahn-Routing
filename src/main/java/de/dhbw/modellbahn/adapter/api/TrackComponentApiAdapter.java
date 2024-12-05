package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;

public interface TrackComponentApiAdapter {
    void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentStatus trackComponentStatus);

    TrackComponentStatus getTrackComponentStatus(TrackComponentId trackComponentId);

}
