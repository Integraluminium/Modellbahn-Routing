package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

public record SetRoutingAlgorithmInstr(RoutingAlgorithm algorithm) implements Instruction {
    @Override
    public void execute(final CommandContext context) throws Exception {
        context.getCurrentRouteBuilder().setRoutingAlgorithm(algorithm);
    }

    @Override
    public void trace(final CommandContext context) {
        context.getOutput().println("Set routing algorithm to " + algorithm);
    }
}
