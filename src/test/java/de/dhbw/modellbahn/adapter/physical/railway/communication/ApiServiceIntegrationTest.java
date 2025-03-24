package de.dhbw.modellbahn.adapter.physical.railway.communication;

import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.LocCallsAdapter;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.SystemCallsAdapter;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.application.ConfigReader;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.domain.physical.railway.communication.LocCalls;
import de.dhbw.modellbahn.domain.physical.railway.communication.SystemCalls;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchState;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;
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
        LocId id = new LocId(16397);
        locCalls.setLocSpeed(id, new Speed(100));
    }

    @Test
    @Disabled("This test is disabled because it requires a physical locomotive to be connected to the system")
    void testEmergencyStop() {
        LocId id = new LocId(16397);
        locCalls.emergencyStopLoc(id);
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