package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record ToggleDirectionInstr(LocId locId) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        context.toggleLocomotiveDirection(locId);
        context.getOutput().println("Toggled direction of: " + locId + " to: " + context.getLocomotive(locId).getCurrentFacingDirection().getName());
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Toggle direction of: " + locId);
    }
}
