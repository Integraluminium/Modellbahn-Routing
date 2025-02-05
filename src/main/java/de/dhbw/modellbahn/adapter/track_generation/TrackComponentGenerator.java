package de.dhbw.modellbahn.adapter.track_generation;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.NormalSwitch;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;

import java.util.Collections;
import java.util.List;

public class TrackComponentGenerator {
    private final ConfigReader configReader;
    private final TrackComponentCalls trackComponentCalls;
    private final List<NormalSwitch> normalSwitches;

    public TrackComponentGenerator(ConfigReader configReader, TrackComponentCalls trackComponentCalls) {
        this.configReader = configReader;
        this.trackComponentCalls = trackComponentCalls;

        normalSwitches = createNormalSwitches();
    }

    private List<NormalSwitch> createNormalSwitches() {
        List<ConfigNormalSwitch> switchList = this.configReader.getNormalSwitches();

        return switchList.stream().map(s -> {
            TrackComponentId id = new TrackComponentId(s.id());
            SwitchComponent switchComponent = new SwitchComponent(s.name(), id, this.trackComponentCalls);
            return new NormalSwitch(s.name(), switchComponent, s.root(), s.straight(), s.turnout());
        }).toList();

    }

    public List<NormalSwitch> getNormalSwitches() {
        return Collections.unmodifiableList(normalSwitches);
    }
}
