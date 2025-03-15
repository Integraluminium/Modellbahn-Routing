package de.dhbw.modellbahn.adapter.locomotive.reading;

import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.LocName;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.MaxLocSpeed;

import java.util.List;

public class LocomotiveReader {
    private final ConfigReader configReader;

    public LocomotiveReader(ConfigReader configReader) {
        this.configReader = configReader;
    }

    public List<Locomotive> readLocomotives(LocCalls locCalls) {
        return this.configReader.getLocomotives().stream().map(loc -> new Locomotive(
                new LocName(loc.name()),
                new LocId(loc.id(), configReader),
                new MaxLocSpeed(loc.maxSpeed()),
                loc.accelerationTime(),
                new Distance(loc.accelerationDistance()),
                new Distance(loc.decelerationDistance()),
                GraphPoint.of(loc.position()),
                GraphPoint.of(loc.facingDirection()),
                locCalls
        )).toList();
    }
}
