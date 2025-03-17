package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record ToggleDirectionInstr(LocId locId) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.toggleLocomotiveDirection(locId);
        context.getOutput().println("Toggled direction of: " + locId + " to: " + context.getLocomotive(locId).getCurrentFacingDirection().getName());
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Toggle direction of: " + locId);
    }
}
