package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocName;
import de.dhbw.modellbahn.domain.locomotive.attributes.MaxLocSpeed;

public class MockedLocomotive extends Locomotive {
    public MockedLocomotive(GraphPoint startPosition, GraphPoint startFacingDirection) {
        this(new MaxLocSpeed(1.0), 1000, new Distance(500), 1000, new Distance(500), startPosition, startFacingDirection);
    }

    public MockedLocomotive(MaxLocSpeed maxLocSpeed, long accelerationTime, Distance accelerationDistance, long decelerationTime, Distance decelerationDistance, GraphPoint startPosition, GraphPoint startFacingDirection) {
        super(new LocName("MockedLoc"), new LocId(1), maxLocSpeed, false, accelerationTime, accelerationDistance, decelerationTime, decelerationDistance, startPosition, startFacingDirection, new MockedLockCalls());
    }

    public MockedLocomotive(int id, GraphPoint startPosition, GraphPoint startFacingDirection) {
        super(new LocName("MockedLoc-" + id), new LocId(id), new MaxLocSpeed(1.0), false, 10, new Distance(5), 10, new Distance(5), startPosition, startFacingDirection, new MockedLockCalls());
    }
}
