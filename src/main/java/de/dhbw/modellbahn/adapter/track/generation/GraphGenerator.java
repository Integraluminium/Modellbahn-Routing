package de.dhbw.modellbahn.adapter.track.generation;

import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.*;
import de.dhbw.modellbahn.domain.track.components.SignalComponent;
import de.dhbw.modellbahn.domain.track.components.SwitchComponent;
import de.dhbw.modellbahn.domain.track.components.TrackComponentId;
import de.dhbw.modellbahn.domain.track.components.TrackContactComponent;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GraphGenerator {
    private final ConfigReader configReader;
    private final TrackComponentCalls trackComponentCalls;

    public GraphGenerator(ConfigReader configReader, TrackComponentCalls trackComponentCalls) {
        this.configReader = configReader;
        this.trackComponentCalls = trackComponentCalls;
    }

    private List<NormalSwitch> generateNormalSwitches() {
        List<ConfigNormalSwitch> switchList = this.configReader.getNormalSwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent = this.createSwitchComponent(s.id());
            return new NormalSwitch(new PointName(s.name()), switchComponent, new PointName(s.root()),
                    new PointName(s.straight()), new PointName(s.turnout()));
        }).toList();
    }

    private List<ThreeWaySwitch> generateThreeWaySwitches() {
        List<ConfigThreeWaySwitch> switchList = this.configReader.getThreeWaySwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent1 = this.createSwitchComponent(s.id1());
            SwitchComponent switchComponent2 = this.createSwitchComponent(s.id2());
            return new ThreeWaySwitch(new PointName(s.name()), switchComponent1, switchComponent2, new PointName(s.root()),
                    new PointName(s.straight()), new PointName(s.left()), new PointName(s.right()));
        }).toList();
    }

    private List<CrossSwitch> generateCrossSwitches() {
        List<ConfigCrossSwitch> switchList = this.configReader.getCrossSwitches();

        return switchList.stream().map(s -> {
            SwitchComponent switchComponent = this.createSwitchComponent(s.id());
            return new CrossSwitch(new PointName(s.name()), switchComponent, new PointName(s.root1()),
                    new PointName(s.root2()), new PointName(s.turnout1()), new PointName(s.turnout2()));
        }).toList();
    }

    private SwitchComponent createSwitchComponent(int id) {
        TrackComponentId trackComponentId = new TrackComponentId(id);
        return new SwitchComponent(trackComponentId, this.trackComponentCalls);
    }

    private List<TrackContact> generateTrackContacts() {
        List<ConfigTrackContact> trackContactList = this.configReader.getTrackContacts();

        return trackContactList.stream().map(t -> {
            TrackContactComponent trackContactComponent = new TrackContactComponent(new TrackComponentId(t.id()));
            return new TrackContact(new PointName(t.name()), trackContactComponent);
        }).toList();
    }

    private List<Signal> generateSignals() {
        List<ConfigSignal> signalList = this.configReader.getSignals();

        return signalList.stream().map(s -> {
            SignalComponent signalComponent = new SignalComponent(new TrackComponentId(s.id()), this.trackComponentCalls);
            return new Signal(new PointName(s.name()), signalComponent);
        }).toList();
    }

    private List<GraphPoint> generateVirtualPoints() {
        List<ConfigVirtualPoint> virtualPointList = this.configReader.getVirtualPoints();

        return virtualPointList.stream().map(p -> new GraphPoint(new PointName(p.name()))).toList();
    }

    private List<GraphConnection> generateGraphConnections(List<NormalSwitch> normalSwitches, List<ThreeWaySwitch> threeWaySwitches, List<CrossSwitch> crossSwitches,
                                                           List<TrackContact> trackContacts, List<Signal> signals, List<GraphPoint> virtualPoints) {
        List<ConfigConnection> connectionList = this.configReader.getConnections();
        List<GraphPoint> allComponents = Stream.of(normalSwitches, threeWaySwitches, crossSwitches, trackContacts, signals, virtualPoints)
                .flatMap(Collection::stream).collect(Collectors.toList());
        Map<String, GraphPoint> graphPointMap = allComponents.stream().collect(Collectors.toMap(point -> point.getName().name(), point -> point));

        return connectionList.stream().map(c -> {
            GraphPoint startPoint = graphPointMap.get(c.source());
            GraphPoint destination = graphPointMap.get(c.destination());
            Distance distance = new Distance(c.distance());
            Height height = new Height(c.height());
            WeightedEdge weightedEdge = new WeightedEdge(destination, distance, height, c.electrified());
            return new GraphConnection(startPoint, weightedEdge);
        }).toList();
    }

    public Graph generateGraph() {
        List<NormalSwitch> normalSwitches = generateNormalSwitches();
        List<ThreeWaySwitch> threeWaySwitches = generateThreeWaySwitches();
        List<CrossSwitch> crossSwitches = generateCrossSwitches();
        List<TrackContact> trackContacts = generateTrackContacts();
        List<Signal> signals = generateSignals();
        List<GraphPoint> virtualPoints = generateVirtualPoints();
        List<GraphConnection> graphConnections = generateGraphConnections(
                normalSwitches, threeWaySwitches, crossSwitches, trackContacts, signals, virtualPoints);

        Graph returnGraph = new Graph();
        graphConnections.forEach(returnGraph::addEdge);
        return returnGraph;
    }
}
