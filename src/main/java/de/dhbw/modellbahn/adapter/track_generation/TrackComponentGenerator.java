package de.dhbw.modellbahn.adapter.track_generation;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.*;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import de.dhbw.modellbahn.domain.track_components.TrackSensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackComponentGenerator {
    private final ConfigReader configReader;
    private final TrackComponentCalls trackComponentCalls;
    private final List<NormalSwitch> normalSwitches;
    private final List<ThreeWaySwitch> threeWaySwitches;
    private final List<CrossSwitch> crossSwitches;
    private final List<TrackContact> trackContacts;
    private final List<GraphPoint> virtualPoints;

    public TrackComponentGenerator(ConfigReader configReader, TrackComponentCalls trackComponentCalls) {
        this.configReader = configReader;
        this.trackComponentCalls = trackComponentCalls;

        this.normalSwitches = generateNormalSwitches();
        this.crossSwitches = generateCrossSwitches();
        this.threeWaySwitches = generateThreeWaySwitches();
        this.trackContacts = generateTrackContacts();
        this.virtualPoints = generateVirtualPoints();
    }

    private List<NormalSwitch> generateNormalSwitches() {
        List<ConfigNormalSwitch> switchList = this.configReader.getNormalSwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent = this.createSwitchComponent(s.name(), s.id());
            return new NormalSwitch(s.name(), switchComponent, s.root(), s.straight(), s.turnout());
        }).toList();
    }

    private List<ThreeWaySwitch> generateThreeWaySwitches() {
        List<ConfigThreeWaySwitch> switchList = this.configReader.getThreeWaySwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent1 = this.createSwitchComponent(s.name() + "1", s.id1());
            SwitchComponent switchComponent2 = this.createSwitchComponent(s.name() + "2", s.id2());
            return new ThreeWaySwitch(s.name(), switchComponent1, switchComponent2, s.root(), s.straight(), s.left(), s.right());
        }).toList();
    }

    private List<CrossSwitch> generateCrossSwitches() {
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

    private List<TrackContact> generateTrackContacts() {
        List<ConfigTrackContact> trackContactList = this.configReader.getTrackContacts();

        return trackContactList.stream().map(t -> {
            TrackSensor trackSensor = new TrackSensor(t.name(), new TrackComponentId(t.id()));
            return new TrackContact(t.name(), trackSensor);
        }).toList();
    }

    private List<GraphPoint> generateVirtualPoints() {
        List<ConfigVirtualPoint> virtualPointList = this.configReader.getVirtualPoints();

        return virtualPointList.stream().map(p -> new GraphPoint(p.name())).toList();
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

    public List<TrackContact> getTrackContacts() {
        return Collections.unmodifiableList(trackContacts);
    }

    public List<GraphPoint> getVirtualPoints() {
        return Collections.unmodifiableList(virtualPoints);
    }

    public List<GraphPoint> getAllComponents() {
        List<GraphPoint> allComponents = new ArrayList<>();
        allComponents.addAll(this.normalSwitches);
        allComponents.addAll(this.threeWaySwitches);
        allComponents.addAll(this.crossSwitches);
        allComponents.addAll(this.trackContacts);
        allComponents.addAll(this.virtualPoints);
        return Collections.unmodifiableList(allComponents);
    }
}
