package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record ModifyLocSpeedInstr(LocId locId, Speed speed) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        context.getLocomotive(locId).setCurrentSpeed(speed);
    }

    @Override
    public void trace(final CommandContext context) {

    }
}
