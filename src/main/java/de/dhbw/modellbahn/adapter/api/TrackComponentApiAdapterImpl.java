package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.adapter.api.dto.SwitchingAccessoriesCommand;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;

public class TrackComponentApiAdapterImpl implements TrackComponentApiAdapter {
    private final ApiAdapterImpl apiAdapter;

    public TrackComponentApiAdapterImpl(ApiAdapterImpl apiAdapter) {
        this.apiAdapter = apiAdapter;
    }

    @Override
    public void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentStatus trackComponentStatus) {
        apiAdapter.sendRequest("/loc/switch_accessory", new SwitchingAccessoriesCommand(
                apiAdapter.getSenderHash(),
                false,
                trackComponentId.id(),
                trackComponentStatus.status(),
                1,
                null
        ));
    }

    @Override
    public TrackComponentStatus getTrackComponentStatus(TrackComponentId trackComponentId) {
        apiAdapter.sendRequest("/loc/switch_accessory", new SwitchingAccessoriesCommand(
                apiAdapter.getSenderHash(),
                false,
                trackComponentId.id(),
                0,
                0,
                null
        ));

        return new TrackComponentStatus(0);    // TODO
    }
}
