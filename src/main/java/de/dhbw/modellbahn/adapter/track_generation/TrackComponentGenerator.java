package de.dhbw.modellbahn.adapter.track_generation;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.CrossSwitch;
import de.dhbw.modellbahn.domain.graph.NormalSwitch;
import de.dhbw.modellbahn.domain.graph.ThreeWaySwitch;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;

import java.util.Collections;
import java.util.List;

public class TrackComponentGenerator {
    private final ConfigReader configReader;
    private final TrackComponentCalls trackComponentCalls;
    private final List<NormalSwitch> normalSwitches;
    private final List<ThreeWaySwitch> threeWaySwitches;
    private final List<CrossSwitch> crossSwitches;

    public TrackComponentGenerator(ConfigReader configReader, TrackComponentCalls trackComponentCalls) {
        this.configReader = configReader;
        this.trackComponentCalls = trackComponentCalls;

        this.normalSwitches = createNormalSwitches();
        this.crossSwitches = createCrossSwitches();
        this.threeWaySwitches = createThreeWaySwitches();
    }

    private List<NormalSwitch> createNormalSwitches() {
        List<ConfigNormalSwitch> switchList = this.configReader.getNormalSwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent = this.createSwitchComponent(s.name(), s.id());
            return new NormalSwitch(s.name(), switchComponent, s.root(), s.straight(), s.turnout());
        }).toList();
    }

    private List<ThreeWaySwitch> createThreeWaySwitches() {
        List<ConfigThreeWaySwitch> switchList = this.configReader.getThreeWaySwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent1 = this.createSwitchComponent(s.name() + "1", s.id1());
            SwitchComponent switchComponent2 = this.createSwitchComponent(s.name() + "2", s.id2());
            return new ThreeWaySwitch(s.name(), switchComponent1, switchComponent2, s.root(), s.straight(), s.left(), s.right());
        }).toList();
    }

    private List<CrossSwitch> createCrossSwitches() {
        List<ConfigCrossSwitch> switchList = this.configReader.getCrossSwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent = this.createSwitchComponent(s.name(), s.id());
            return new CrossSwitch(s.name(), switchComponent, s.root1(), s.root2(), s.turnout1(), s.turnout2());
        }).toList();
    }

    private SwitchComponent createSwitchComponent(String name, int id) {
        TrackComponentId trackComponentId = new TrackComponentId(id);
        return new SwitchComponent(name, trackComponentId, this.trackComponentCalls);
    }

    public List<NormalSwitch> getNormalSwitches() {
        return Collections.unmodifiableList(normalSwitches);
    }

    public List<ThreeWaySwitch> getThreeWaySwitches() {
        return Collections.unmodifiableList(threeWaySwitches);
    }

    public List<CrossSwitch> getCrossSwitches() {
        return Collections.unmodifiableList(crossSwitches);
    }
}
