package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.MockedTrackComponentCalls;
import de.dhbw.modellbahn.application.routing.actions.ChangeSwitchStateAction;
import de.dhbw.modellbahn.application.routing.actions.LocSpeedAction;
import de.dhbw.modellbahn.application.routing.actions.RoutingAction;
import de.dhbw.modellbahn.application.routing.actions.WaitAction;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.NormalSwitch;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.domain.physical.railway.components.SwitchComponent;
import de.dhbw.modellbahn.domain.physical.railway.components.TrackComponentId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RouteTest {

    @Test
    void driveRoute() {
        GraphPoint pointA = GraphPoint.of("A");
        GraphPoint pointC = GraphPoint.of("C");
        GraphPoint pointD = GraphPoint.of("D");

        SwitchComponent switchComponent = new SwitchComponent(new TrackComponentId(0), new MockedTrackComponentCalls());
        NormalSwitch normalSwitch = new NormalSwitch(new PointName("B"), switchComponent, pointA.getName(), pointC.getName(), pointD.getName());

        Locomotive loc = new MockedLocomotive(pointA, normalSwitch);

        List<RoutingAction> actionList = List.of(
                new LocSpeedAction(loc, new Speed(100)),
                new WaitAction(0),
                new ChangeSwitchStateAction(normalSwitch, pointA, pointC),
                new LocSpeedAction(loc, new Speed(0))
        );
        Route route = new Route(loc, actionList, pointD, pointA, 0);

        route.driveRoute();

        assertEquals(route.getActionList(), actionList);
        assertEquals(loc.getCurrentPosition(), pointD);
        assertEquals(loc.getCurrentFacingDirection(), pointA);
    }
}