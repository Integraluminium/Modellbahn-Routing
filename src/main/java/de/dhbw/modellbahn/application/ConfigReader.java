package de.dhbw.modellbahn.application;

import de.dhbw.modellbahn.adapter.locomotive.reading.ConfigLocomotive;
import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiConfig;
import de.dhbw.modellbahn.adapter.track.generation.*;

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

    List<ConfigBufferStop> getBufferStops();

    List<ConfigSignal> getSignals();
}
