package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.GraphPoint;

public class MockedLocomotive extends Locomotive {
    public MockedLocomotive(GraphPoint startPosition, GraphPoint startFacingDirection) {
        this(new MaxLocSpeed(1.0), 1000, new Distance(500), new Distance(500), startPosition, startFacingDirection);
    }

    public MockedLocomotive(MaxLocSpeed maxLocSpeed, long accelerationTime, Distance accelerationDistance, Distance decelerationDistance, GraphPoint startPosition, GraphPoint startFacingDirection) {
        super(new LocName("MockedLoc"), new LocId(1), maxLocSpeed, accelerationTime, accelerationDistance, decelerationDistance, startPosition, startFacingDirection, new MockedLockCalls());
    }
}
