package de.dhbw.modellbahn.adapter.moba.communication;

import de.dhbw.modellbahn.adapter.moba.communication.calls.LocCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.track_components.SwitchState;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ApiServiceIntegrationTest {
    private ConfigReader configReader;
    private ApiService apiService;

    private LocCalls locCalls;
    private SystemCalls systemCalls;
    private TrackComponentCalls trackComponentCalls;

    @BeforeAll
    static void beforeAll() {

    }

    @BeforeEach
    void setUp() {
        configReader = new YAMLConfigReader();
        apiService = new ApiService(25438); // just a random number - stolen from central station

        locCalls = new LocCallsAdapter(apiService);
        systemCalls = new SystemCallsAdapter(apiService);
        trackComponentCalls = new TrackComponentCallsAdapter(apiService);
    }

    @Test
    @Disabled("This test is disabled because it requires a physical locomotive to be connected to the system")
    void testSystemStop() {
        systemCalls.systemStop();
        System.out.println("System stopped");
    }

    @Test
    @Disabled("This test is disabled because it requires a physical locomotive to be connected to the system")
    void testSystemGo() {
        systemCalls.systemGo();
        System.out.println("System started");
    }

    @Test
    @Disabled("This test is disabled because it requires a physical locomotive to be connected to the system")
    void testLocomotive() {
        LocId id = new LocId(16389, configReader);

    }

    /*
body = {
  "hash_value": 22328,
  "response": False,
  "loc_id": 12292,
  "position": 0,
  "power": 1,
  "value": None
}
requests.post(url + '/loc/switch_accessory', json=body)
     */
    @Test
    @Disabled
    void testTrackComponent() {
        TrackComponentId id = new TrackComponentId(12291); // Decoupler
        SwitchState state = SwitchState.DIVERGENT;
        trackComponentCalls.setSwitchComponentStatus(id, state);
    }
}