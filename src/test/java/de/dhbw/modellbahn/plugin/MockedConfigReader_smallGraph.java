package de.dhbw.modellbahn.plugin;

import de.dhbw.modellbahn.adapter.track.generation.*;
import de.dhbw.modellbahn.domain.ConfigReader;

import java.util.List;

public class MockedConfigReader_smallGraph extends MockedConfigReader implements ConfigReader {

    // MockedGraph.png shows the output graph of the MockedConfigReader
    @Override
    public List<ConfigConnection> getConnections() {
        return List.of(
                new ConfigConnection("A", "B", 20, 0, true),
//                new ConfigConnection("B", "A", 20, 0, true),

                new ConfigConnection("A", "C", 20, 0, true),
//                new ConfigConnection("C", "A", 20, 0, true),

                new ConfigConnection("A", "Z", 40, 20, true),
//                new ConfigConnection("Z", "A", 40, -20, true),

                new ConfigConnection("B", "C", 40, 0, true),
//                new ConfigConnection("C", "B", 40, 0, true),

                new ConfigConnection("B", "F", 20, 0, true),
//                new ConfigConnection("F", "B", 20, 0, true),

//                new ConfigConnection("F", "C", 30, 0, true),
                new ConfigConnection("C", "F", 30, 0, true)
        );
    }

    @Override
    public List<ConfigCrossSwitch> getCrossSwitches() {
        return List.of();
    }

    @Override
    public List<ConfigNormalSwitch> getNormalSwitches() {
        return List.of(
                new ConfigNormalSwitch("A", 1, "B", "C", "Z"),
                new ConfigNormalSwitch("B", 2, "A", "F", "C"),
                new ConfigNormalSwitch("C", 3, "F", "A", "B")
        );
    }

    @Override
    public List<ConfigThreeWaySwitch> getThreeWaySwitches() {
        return List.of();
    }

    @Override
    public List<ConfigTrackContact> getTrackContacts() {
        return List.of();
    }

    @Override
    public List<ConfigVirtualPoint> getVirtualPoints() {
        return List.of(new ConfigVirtualPoint("F"), new ConfigVirtualPoint("Z"));
    }

    @Override
    public List<ConfigSignal> getSignals() {
        return List.of();
    }
}
