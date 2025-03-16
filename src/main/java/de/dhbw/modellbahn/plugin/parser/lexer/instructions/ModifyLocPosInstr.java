package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record ModifyLocPosInstr(LocId locId, GraphPoint destination) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        context.getLocomotive(locId).setCurrentPosition(destination);
    }
}
