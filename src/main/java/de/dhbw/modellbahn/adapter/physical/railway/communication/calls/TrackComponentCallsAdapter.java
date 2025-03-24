package de.dhbw.modellbahn.adapter.physical.railway.communication.calls;

import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiService;
import de.dhbw.modellbahn.adapter.physical.railway.communication.dto.SwitchingAccessoriesCommand;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.physical.railway.components.SignalState;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchState;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;

import java.util.logging.Logger;

public class TrackComponentCallsAdapter implements TrackComponentCalls {
    private static final Logger logger = Logger.getLogger(TrackComponentCallsAdapter.class.getSimpleName());
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
        logger.info("setTrackComponentStatus<" + apiAdapter.getSenderHash() + ">: " + trackComponentId.id() + "->" + trackComponentStatus);
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
