package de.dhbw.modellbahn.domain.locomotive;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {
    private static final LocId locId = new LocId(16389);

    @Test
    void testGetLocId() {
        Locomotive loc = new Locomotive(locId);

        assertEquals(loc.getLocId(), locId);
    }

}