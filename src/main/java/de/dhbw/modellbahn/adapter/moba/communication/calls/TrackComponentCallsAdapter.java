package de.dhbw.modellbahn.adapter.moba.communication.calls;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.dto.SwitchingAccessoriesCommand;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.trackcomponents.SignalState;
import de.dhbw.modellbahn.domain.trackcomponents.SwitchState;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;

public class TrackComponentCallsAdapter implements TrackComponentCalls {
    private final ApiService apiAdapter;

    public TrackComponentCallsAdapter(ApiService apiAdapter) {
        this.apiAdapter = apiAdapter;
    }


    private int getTrackComponentStatus(TrackComponentId trackComponentId) {
        apiAdapter.sendRequest("/loc/switch_accessory", new SwitchingAccessoriesCommand(
                apiAdapter.getSenderHash(),
                false,
                trackComponentId.id(),
                0,
                0,
                null
        ));

        return 0;    // TODO
    }

    private void setTrackComponentStatus(TrackComponentId trackComponentId, int trackComponentStatus) {
        apiAdapter.sendRequest("/loc/switch_accessory", new SwitchingAccessoriesCommand(
                apiAdapter.getSenderHash(),
                false,
                trackComponentId.id(),
                trackComponentStatus,
                1,
                null
        ));
    }

    @Override
    public void setSwitchComponentStatus(final TrackComponentId trackComponentId, final SwitchState switchState) {
        setTrackComponentStatus(trackComponentId, switchState.getValue());
    }

    @Override
    public SwitchState getSwitchComponentStatus(final TrackComponentId trackComponentId) {
        return null;
    }

    @Override
    public void setSignalComponentStatus(final TrackComponentId trackComponentId, final SignalState signalState) {
        setTrackComponentStatus(trackComponentId, signalState.getValue());
    }

    @Override
    public SwitchState getSignalComponentStatus(final TrackComponentId trackComponentId) {
        return null;
    }
}
