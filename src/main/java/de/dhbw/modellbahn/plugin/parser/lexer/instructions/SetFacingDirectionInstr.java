package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record SetFacingDirectionInstr(LocId locId, GraphPoint facing) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().setFacingDirectionForLoc(loc, facing);
    }
}
