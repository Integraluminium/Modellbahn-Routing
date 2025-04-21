package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.application.routing.building.PathNotPossibleException;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record GenerateRouteInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) throws InstructionException {
        try {
            context.generateRoute();
        } catch (PathNotPossibleException e) {
            throw new InstructionException("Error while generating route", e);
        }
    }

    @Override
    public void trace(final CommandContext context) {

    }
}
