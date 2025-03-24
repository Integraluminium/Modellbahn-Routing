package de.dhbw.modellbahn.parser.instructions;


import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

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
