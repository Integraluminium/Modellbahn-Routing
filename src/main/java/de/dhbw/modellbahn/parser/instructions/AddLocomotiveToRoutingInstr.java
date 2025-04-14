package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record AddLocomotiveToRoutingInstr(LocId locId) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws InstructionException {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().addLocomotive(loc);
    }

    @Override
    public void trace(CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getOutput().println("Added locomotive: " + loc.getName().name());
    }
}
