package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.graph.TrackContact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {
    private static final LocId locId = new LocId(16389);

    @Test
    void testGetLocId() {
        Locomotive loc = new Locomotive(locId, new TrackContact());

        assertEquals(loc.getLocId(), locId);
    }

}