package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record ModifyLocFacingInstr(LocId locId, GraphPoint facingPoint) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.getLocomotive(locId).setCurrentFacingDirection(facingPoint);
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Modify virtual facing direction of " + context.getLocomotive(locId).getLocId() + " to " + facingPoint.getName());
    }
}
