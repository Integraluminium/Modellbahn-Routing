package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record SetDestinationInstr(LocId locId, GraphPoint destination) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        if (!context.existsLocomotive(locId)) {
            throw new Exception("Locomotive with id " + locId + " does not exist");
        }

        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().setDestinationForLoc(loc, destination);
    }

    @Override
    public void trace(final CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getOutput().println("Set destination <" + destination.getName().name() + "> for locomotive: <" + loc.getName().name() + ">");
    }
}
