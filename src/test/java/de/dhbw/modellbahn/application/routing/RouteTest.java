package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedTrackComponentCalls;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.NormalSwitch;
import de.dhbw.modellbahn.domain.graph.PointName;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.track_components.SwitchComponent;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteTest {

    @Test
    void driveRoute() {
        GraphPoint pointA = new GraphPoint(new PointName("A"));
        GraphPoint pointC = new GraphPoint(new PointName("C"));
        GraphPoint pointD = new GraphPoint(new PointName("D"));

        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(0), new MockedTrackComponentCalls());
        NormalSwitch normalSwitch = new NormalSwitch(new PointName("B"), switchComponent, pointA.getName(), pointC.getName(), pointD.getName());

        Locomotive loc = new MockedLocomotive(pointA, normalSwitch);

        List<RoutingAction> actionList = List.of(
                new LocSpeedAction(loc, new Speed(100)),
                new WaitAction(0),
                new ChangeSwitchStateAction(normalSwitch, pointA, pointC),
                new LocSpeedAction(loc, new Speed(0))
        );
        Route route = new Route(loc, actionList, pointD, pointA);

        route.driveRoute();

        assertEquals(route.getActionList(), actionList);
        assertEquals(loc.getCurrentPosition(), pointD);
        assertEquals(loc.getCurrentFacingDirection(), pointA);
    }
}