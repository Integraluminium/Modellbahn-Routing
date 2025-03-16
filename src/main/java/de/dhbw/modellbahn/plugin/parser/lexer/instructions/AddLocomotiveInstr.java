package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record AddLocomotiveInstr(LocId locId) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().addLocomotive(loc);
    }

    @Override
    public void trace(CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getOutput().println("Added locomotive: " + loc.getName().name());
    }
}
