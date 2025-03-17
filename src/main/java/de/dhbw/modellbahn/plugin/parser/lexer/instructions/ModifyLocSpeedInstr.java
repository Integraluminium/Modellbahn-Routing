package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record ModifyLocSpeedInstr(LocId locId, Speed speed) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.getLocomotive(locId).setCurrentSpeed(speed);
    }

    @Override
    public void trace(final CommandContext context) {

    }
}
