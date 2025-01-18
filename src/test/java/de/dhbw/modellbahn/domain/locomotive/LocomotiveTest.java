package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.plugin.CsvReader;
import de.dhbw.modellbahn.domain.ConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocomotiveTest {
    private static LocId locId;

    @BeforeAll
    static void createConfigReader() {
        ConfigReader configReader = new CsvReader();
        locId = new LocId(16389, configReader);
    }

    @Test
    void testGetLocId() {
        Locomotive loc = new Locomotive(locId);

        assertEquals(loc.getLocId(), locId);
    }

}