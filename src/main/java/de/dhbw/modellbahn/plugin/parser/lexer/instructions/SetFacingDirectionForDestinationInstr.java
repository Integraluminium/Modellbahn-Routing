package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;


/**
 * Set the facing direction for the Routing destination of a locomotive.
 *
 * @param locId
 * @param facing
 */
public record SetFacingDirectionForDestinationInstr(LocId locId, GraphPoint facing) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().setLocFacingDirectionForDestination(loc, facing);
    }

    @Override
    public void trace(final CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getOutput().println("Set facing direction for locomotive: <" + loc.getName().name() + "> to <" + facing.getName().name() + ">");
    }
}
