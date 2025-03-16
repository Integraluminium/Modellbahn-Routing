package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

import java.io.PrintStream;

public record ListLocomotivesInstr() implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        PrintStream output = context.getOutput();
        for (LocId locId : context.getLocInfos()) {
            Locomotive loc = context.getLocomotive(locId);
            output.println(" - ID: " + locId.id() +
                    ", Name: '" + loc.getName().name() + "'" +
                    ", Position: " + loc.getCurrentPosition() +
                    ", Facing: " + loc.getCurrentFacingDirection());
        }
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("List all locomotives");
    }
}
