package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record SetOptimizationInstr(LocId locId, RoutingOptimization optimization) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().setRouteOptimization(loc, optimization);
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Set Routing Optimization for " + locId + " to " + optimization);
    }
}
