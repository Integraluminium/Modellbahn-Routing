package de.dhbw.modellbahn.domain;

import de.dhbw.modellbahn.adapter.api.ApiConfig;
import de.dhbw.modellbahn.adapter.track_generation.*;

import java.util.List;

public interface ConfigReader {
    List<Integer> getValidLocIds();

    List<ConfigLocomotive> getLocomotives();

    ApiConfig getApiConfig();

    List<ConfigConnection> getConnections();

    List<ConfigCrossSwitch> getCrossSwitches();

    List<ConfigNormalSwitch> getNormalSwitches();

    List<ConfigThreeWaySwitch> getThreeWaySwitches();

    List<ConfigTrackContact> getTrackContacts();

    List<ConfigVirtualPoint> getVirtualPoints();

    List<ConfigSignal> getSignals();
}
