package de.dhbw.modellbahn.plugin.parser.lexer.instructions;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;

public record RemoveLocomotiveFromTrackInstr(LocId locId) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        GraphPoint notOnTrackPoint = GraphPoint.of("NotOnTrack");
        Locomotive locomotive = context.getLocomotive(locId);

        locomotive.setCurrentPosition(notOnTrackPoint);
        locomotive.setCurrentFacingDirection(notOnTrackPoint);
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Remove locomotive " + locId + " from track");
    }
}
