package de.dhbw.modellbahn.adapter.locomotive.reading;

import de.dhbw.modellbahn.application.ConfigReader;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocName;
import de.dhbw.modellbahn.domain.locomotive.attributes.MaxLocSpeed;
import de.dhbw.modellbahn.domain.physical.railway.communication.LocCalls;

import java.util.List;

public class LocomotiveReader {
    private final ConfigReader configReader;

    public LocomotiveReader(ConfigReader configReader) {
        this.configReader = configReader;
    }

    public List<Locomotive> readLocomotives(LocCalls locCalls) {
        return this.configReader.getLocomotives().stream().map(loc -> new Locomotive(
                new LocName(loc.name()),
                new LocId(loc.id()),
                new MaxLocSpeed(loc.maxSpeed()),
                loc.accelerationTime(),
                new Distance(loc.accelerationDistance()),
                loc.decelerationTime(),
                new Distance(loc.decelerationDistance()),
                GraphPoint.of(loc.position()),
                GraphPoint.of(loc.facingDirection()),
                locCalls
        )).toList();
    }
}
