package de.dhbw.modellbahn.parser;

import de.dhbw.modellbahn.application.repositories.LocomotiveRepository;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.physical.railway.communication.SystemCalls;
import de.dhbw.modellbahn.parser.instructions.Instruction;
import de.dhbw.modellbahn.parser.instructions.RemoveLocomotiveFromTrackInstr;
import de.dhbw.modellbahn.parser.lexer.CommandContext;

import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

public class CommandExecutor {
    private final CommandContext commandContext;
    private final Logger logger = Logger.getLogger(CommandExecutor.class.getName());

    public CommandExecutor(LocomotiveRepository locomotiveRepository, Graph graph, SystemCalls systemCalls, PrintStream stream) {
        this.commandContext = new CommandContext(locomotiveRepository, graph, systemCalls, stream, this);
    }

    public void execute(List<Instruction> instructions, boolean trace) throws Exception {
        try {
            for (Instruction instruction : instructions) {
                if (trace) {
                    instruction.trace(commandContext);
                }
                instruction.execute(commandContext);
            }
        } catch (Exception e) {
            commandContext.getSystemCalls().systemStop();
            logger.severe("Stopped System due to error during execution of last instruction > " + e.getMessage());
            setAllLocomotivesNotOnTrack();
            throw e;
        }

    }

    private void setAllLocomotivesNotOnTrack() throws Exception {
        logger.warning("Setting all locomotives to 'not on track'");
        List<RemoveLocomotiveFromTrackInstr> removeInstr = StreamSupport.stream(commandContext.getLocInfos().spliterator(), false)
                .map(RemoveLocomotiveFromTrackInstr::new).toList();
        for (Instruction inst : removeInstr) {
            try {
                inst.execute(commandContext);
            } catch (Exception e) {
                throw new Exception("Failed to set all locomotives not on track", e);
            }
        }
    }
}
