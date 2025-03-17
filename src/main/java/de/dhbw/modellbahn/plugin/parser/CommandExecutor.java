package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.port.moba.communication.SystemCalls;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.plugin.parser.lexer.CommandContext;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction;

import java.io.PrintStream;
import java.util.List;

public class CommandExecutor {
    private final CommandContext commandContext;

    public CommandExecutor(LocomotiveRepository locomotiveRepository, Graph graph, SystemCalls systemCalls, PrintStream stream) {
        this.commandContext = new CommandContext(locomotiveRepository, graph, systemCalls, stream);
    }

    public void execute(List<Instruction> instructions, boolean trace) throws Exception {
        for (Instruction instruction : instructions) {
            if (trace) {
                instruction.trace(commandContext);
            }
            instruction.execute(commandContext);
        }
    }
}
