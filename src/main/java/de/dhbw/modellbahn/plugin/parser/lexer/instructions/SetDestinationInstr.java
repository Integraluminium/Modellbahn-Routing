package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record SetDestinationInstr(LocId locId, GraphPoint destination) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        if (context.existsLocomotive(locId)) {
            throw new Exception("Locomotive with id " + locId + " does not exist");
        }

        Locomotive loc = context.getLocomotive(locId);
        context.getOutput().println("Set destination for locomotive " + loc.getName().name() + " to " + destination.getName().name());
        context.getCurrentRouteBuilder().setDestinationForLoc(loc, destination);
    }
}
