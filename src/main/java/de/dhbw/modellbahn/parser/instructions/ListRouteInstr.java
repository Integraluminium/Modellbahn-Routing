package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.application.routing.Route;
import de.dhbw.modellbahn.application.routing.actions.RoutingAction;
import de.dhbw.modellbahn.application.routing.building.RouteBuilder;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

import java.io.PrintStream;
import java.util.List;

public record ListRouteInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        RouteBuilder routeBuilder = context.getCurrentRouteBuilder();
        PrintStream output = context.getOutput();
        Iterable<Locomotive> locomotivesWithRoute = routeBuilder.getLocomotivesWithRoute();

        if (locomotivesWithRoute == null || !locomotivesWithRoute.iterator().hasNext()) {
            output.println("No routes have been calculated yet.");
            return;
        }
        output.println("Route for locomotives:");
        for (Locomotive locomotive : locomotivesWithRoute) {
            Route route = routeBuilder.getRouteForLoc(locomotive);
            output.println(" -" + locomotive.getLocId() + ":" + route);
            List<RoutingAction> actionList = route.getActionList();
            actionList.forEach(action -> {
                output.println("  +" + action.toString());
            });
        }
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("List all currently calculated routes.");
    }
}
