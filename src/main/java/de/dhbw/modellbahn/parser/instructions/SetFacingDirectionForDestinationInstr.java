package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.lexer.CommandContext;


/**
 * Set the facing direction for the Routing destination of a locomotive.
 *
 * @param locId
 * @param facing
 */
public record SetFacingDirectionForDestinationInstr(LocId locId, GraphPoint facing) implements Instruction {
    @Override
    public void execute(final CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getCurrentRouteBuilder().setLocFacingDirectionForDestination(loc, facing);
    }

    @Override
    public void trace(final CommandContext context) {
        Locomotive loc = context.getLocomotive(locId);
        context.getOutput().println("Set facing direction for locomotive: <" + loc.getName().name() + "> to <" + facing.getName().name() + ">");
    }
}
