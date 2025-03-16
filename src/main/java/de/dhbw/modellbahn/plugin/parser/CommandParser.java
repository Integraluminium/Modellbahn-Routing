package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.TokenType;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Grammar: [
 * (ADD <locid> (AT <graphpoint>)? (
 * TO <graphpoint> (FACING <graphpoint>)?(USING <optimization>)?
 * )
 * )+
 * DRIVE (CONSIDER (ELECTRIFICATION | HEIGHT))?
 * ]+
 * Parses commands from the lexer and
 */
public class CommandParser {
    private final Lexer lexer;
    private final Parser parser;

    private List<Instruction> instructions;

    public CommandParser(Lexer lexer, Graph graph, LocomotiveRepository locomotiveRepository) {
        this.lexer = lexer;
        this.parser = new Parser(graph, locomotiveRepository);
    }

    public List<Instruction> parse(String input) throws LexerException, ParseException {
        instructions = new ArrayList<>();
        lexer.init(input);

        while (lexer.lookAhead().type() != TokenType.EOF) {
            parseCommand();
        }

        return instructions;
    }

    private void parseCommand() throws LexerException, ParseException {
        // Each command starts with ADD
        lexer.expect(TokenType.ADD_KEYWORD);

        // Parse locomotive ID
        if (lexer.lookAhead().type() != TokenType.LOC_ID) {
            throw new ParseException("Expected locomotive ID after ADD");
        }
        LocId locId = parser.parseLocId(lexer.lookAhead().value());
        lexer.advance();

        // Add locomotive to toConsider
        instructions.add(new AddLocomotiveInstr(locId));

        parseAtStartPosition(locId);

        parseDestination(locId);

        // DRIVE keyword
        lexer.expect(TokenType.DRIVE_COMMAND);

        parseGlobalRoutingModifier();

        // Execute the route generation
        instructions.add(new DriveInstr());
    }

    private void parseGlobalRoutingModifier() throws LexerException, ParseException {
        // Optional CONSIDER (ELECTRIFICATION | HEIGHT)
        if (lexer.lookAhead().type() == TokenType.CONSIDER_KEYWORD) {
            lexer.advance();

            if (lexer.lookAhead().type() == TokenType.ELECTRIFICATION_KEYWORD) {
                lexer.advance();
                instructions.add(new ConsiderElectrificationInstr(true));
            } else if (lexer.lookAhead().type() == TokenType.HEIGHT_KEYWORD) {
                lexer.advance();
                instructions.add(new ConsiderHeightInstr(true));
            } else {
                throw new ParseException("Expected ELECTRIFICATION or HEIGHT after CONSIDER");
            }
        }
    }

    private void parseDestination(final LocId locId) throws LexerException, ParseException {
        // optional: TO <graphpoint> (FACING <graphpoint>)? (USING <optimization>)?
        if (lexer.lookAhead().type() == TokenType.TO_KEYWORD) {
            lexer.advance();

            // Parse destination
            if (lexer.lookAhead().type() != TokenType.GRAPH_POINT) {
                throw new ParseException("Expected graph point after TO");
            }
            GraphPoint destination = parser.parseGraphPoint(lexer.lookAhead().value());
            lexer.advance();

            // Set destination for locomotive
            instructions.add(new SetDestinationInstr(locId, destination));

            // Optional FACING <graphpoint>
            if (lexer.lookAhead().type() == TokenType.FACING_KEYWORD) {
                lexer.advance();
                if (lexer.lookAhead().type() != TokenType.GRAPH_POINT) {
                    throw new ParseException("Expected graph point after FACING");
                }
                GraphPoint facing = parser.parseGraphPoint(lexer.lookAhead().value());
                lexer.advance();

                instructions.add(new SetFacingDirectionInstr(locId, facing));
            }

            // Optional USING <optimization>
            if (lexer.lookAhead().type() == TokenType.USING_KEYWORD) {
                lexer.advance();
                if (lexer.lookAhead().type() != TokenType.OPTIMIZATION) {
                    throw new ParseException("Expected optimization after USING");
                }
                RoutingOptimization optimization = parser.parseOptimization(lexer.lookAhead().value());
                lexer.advance();

                instructions.add(new SetOptimizationInstr(locId, optimization));
            }
        }
    }

    private void parseAtStartPosition(LocId locId) throws LexerException, ParseException {
        // Optional AT <graphpoint>
        if (lexer.lookAhead().type() == TokenType.AT_KEYWORD) {
            lexer.advance();
            if (lexer.lookAhead().type() != TokenType.GRAPH_POINT) {
                throw new ParseException("Expected graph point after AT");
            }
            GraphPoint point = parser.parseGraphPoint(lexer.lookAhead().value());
            lexer.advance();
            instructions.add(new ModifyLocPosInstr(locId, point));
        }
    }

}