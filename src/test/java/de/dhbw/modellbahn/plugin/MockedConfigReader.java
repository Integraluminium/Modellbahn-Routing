package de.dhbw.modellbahn.plugin;

import de.dhbw.modellbahn.adapter.locomotive.reading.ConfigLocomotive;
import de.dhbw.modellbahn.adapter.physical.railway.communication.ApiConfig;
import de.dhbw.modellbahn.adapter.track.generation.*;
import de.dhbw.modellbahn.application.ConfigReader;

import java.util.ArrayList;
import java.util.List;

public class MockedConfigReader implements ConfigReader {

    @Override
    public List<ConfigLocomotive> getLocomotives() {
        return List.of(new ConfigLocomotive(1, "mockedLoc", 1.0, 1000, 200, 1000, 200, "A", "B"));
    }

    @Override
    public ApiConfig getApiConfig() {
        return null;
    }


    // MockedGraph.png shows the output graph of the MockedConfigReader
    @Override
    public List<ConfigConnection> getConnections() {
        List<ConfigConnection> connectionList = new ArrayList<>();
        connectionList.add(new ConfigConnection("D", "A", 20, -10, true));
        connectionList.add(new ConfigConnection("D", "E", 20, 10, true));
        connectionList.add(new ConfigConnection("A", "B", 10, 0, true));
        connectionList.add(new ConfigConnection("A", "E", 30, 0, false));
        connectionList.add(new ConfigConnection("B", "C", 20, 0, true));
        connectionList.add(new ConfigConnection("E", "C", 40, 0, true));
        connectionList.add(new ConfigConnection("E", "F", 70, 0, true));
        connectionList.add(new ConfigConnection("C", "G", 20, 0, true));
        connectionList.add(new ConfigConnection("C", "F", 20, 0, true));
        connectionList.add(new ConfigConnection("F", "G", 40, 0, true));
        return connectionList;
    }

    @Override
    public List<ConfigCrossSwitch> getCrossSwitches() {
        return List.of(new ConfigCrossSwitch("C", 3, "B", "E", "G", "F"));
    }

    @Override
    public List<ConfigNormalSwitch> getNormalSwitches() {
        ConfigNormalSwitch switch1 = new ConfigNormalSwitch("A", 1, "D", "B", "E");
        ConfigNormalSwitch switch2 = new ConfigNormalSwitch("F", 6, "C", "G", "E");
        return List.of(switch1, switch2);
    }

    @Override
    public List<ConfigThreeWaySwitch> getThreeWaySwitches() {
        return List.of(new ConfigThreeWaySwitch("E", 5, 6, "D", "C", "A", "F"));
    }

    @Override
    public List<ConfigTrackContact> getTrackContacts() {
        return List.of(new ConfigTrackContact("D", 4));
    }

    @Override
    public List<ConfigVirtualPoint> getVirtualPoints() {
        return List.of(new ConfigVirtualPoint("B"));
    }

    @Override
    public List<ConfigBufferStop> getBufferStops() {
        return List.of();
    }

    @Override
    public List<ConfigSignal> getSignals() {
        return List.of(new ConfigSignal("G", 8));
    }
}
