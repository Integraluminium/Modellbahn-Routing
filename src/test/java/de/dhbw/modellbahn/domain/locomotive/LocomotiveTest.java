package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.ConfigReader;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocomotiveTest {
    private static final GraphPoint startPoint = new GraphPoint(new PointName("startPoint"));
    private static final GraphPoint facingPoint = new GraphPoint(new PointName("facingPoint"));
    private static LocId locId;
    private static Locomotive locomotive;

    @BeforeAll
    static void beforeAll() {
        ConfigReader configReader = new YAMLConfigReader();
        locId = new LocId(16389, configReader);

        locomotive = new Locomotive(locId, startPoint, facingPoint);
    }

    @Test
    void testGetter() {
        assertEquals(locomotive.getLocId(), locId);
        assertEquals(locomotive.getCurrentPosition(), startPoint);
        assertEquals(locomotive.getCurrentFacingDirection(), facingPoint);
        assertEquals(locomotive.getCurrentSpeed(), new Speed(0));
    }

}