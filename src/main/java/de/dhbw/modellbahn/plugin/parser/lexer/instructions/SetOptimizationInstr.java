package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record SetOptimizationInstr(LocId locId, RoutingOptimization optimization) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().setRouteOptimization(loc, optimization);
    }
}
