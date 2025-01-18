package de.dhbw.modellbahn.adapter.moba.communication.calls;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.TrackComponentStatus;
import de.dhbw.modellbahn.adapter.moba.communication.dto.SwitchingAccessoriesCommand;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;

public class TrackComponentCallsAdapter implements TrackComponentCalls {
    private final ApiService apiAdapter;

    public TrackComponentCallsAdapter(ApiService apiAdapter) {
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
