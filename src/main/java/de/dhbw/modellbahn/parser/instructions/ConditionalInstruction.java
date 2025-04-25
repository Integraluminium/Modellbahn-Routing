package de.dhbw.modellbahn.parser.instructions;

import de.dhbw.modellbahn.parser.lexer.CommandContext;

import java.util.List;
import java.util.logging.Logger;

public class ConditionalInstruction implements Instruction {
    private static final Logger logger = Logger.getLogger(ConditionalInstruction.class.getName());

    private final Instruction condition;
    private final List<Instruction> thenInstructions;
    private final List<Instruction> elseInstructions;

    public ConditionalInstruction(Instruction condition,
                                  List<Instruction> thenInstructions,
                                  List<Instruction> elseInstructions) {
        this.condition = condition;
        this.thenInstructions = thenInstructions;
        this.elseInstructions = elseInstructions;
    }

    @Override
    public void execute(CommandContext context) throws InstructionException {
        try {
            condition.execute(context);
            // If execution succeeds, run "then" branch
            for (Instruction instruction : thenInstructions) {
                instruction.execute(context);
            }
        } catch (InstructionException e) {
            logger.fine("Condition failed, executing else branch: " + e.getMessage());
            // If execution fails, run "else" branch
            for (Instruction instruction : elseInstructions) {
                instruction.execute(context);
            }
        }
    }

    @Override
    public void trace(CommandContext context) {
        context.getOutput().println("IF CONDITION:");
        condition.trace(context);
        context.getOutput().println("THEN:");
        for (Instruction instruction : thenInstructions) {
            instruction.trace(context);
        }
        context.getOutput().println("ELSE:");
        for (Instruction instruction : elseInstructions) {
            instruction.trace(context);
        }
    }
}