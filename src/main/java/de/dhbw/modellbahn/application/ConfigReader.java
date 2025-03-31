package de.dhbw.modellbahn.application;

import de.dhbw.modellbahn.adapter.locomotive.reading.ConfigLocomotive;
import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiConfig;
import de.dhbw.modellbahn.adapter.track.generation.*;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;

import java.util.List;
import java.util.Map;

public interface ConfigReader {

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

    void updateLocomotives(Map<LocId, Locomotive> locomotiveMap);
}
