package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.TokenType;

public class CommandParser {
    private final Lexer lexer;
    private final Actions actions;
    private final Parser parser;

    public CommandParser(Lexer lexer, Graph graph, LocomotiveRepository locomotiveRepository) {
        this.lexer = lexer;
        this.parser = new Parser(graph, locomotiveRepository);
        this.actions = new Actions(graph, locomotiveRepository, null);
    }

    public void parse(String input) throws LexerException, ParseException {
        lexer.init(input);

        while (lexer.lookAhead().type() != TokenType.EOF) {
            parseCommand();
        }
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

        // Add locomotive to consider
        actions.addLocomotiveToConsider(locId);

        // Optional AT <graphpoint>
        if (lexer.lookAhead().type() == TokenType.AT_KEYWORD) {
            lexer.advance();
            if (lexer.lookAhead().type() != TokenType.GRAPH_POINT) {
                throw new ParseException("Expected graph point after AT");
            }
            // Handle setting starting position (not implemented in current Actions)
            lexer.advance();
        }

        // One or more TO statements
        boolean toFound = false;
        while (lexer.lookAhead().type() == TokenType.TO_KEYWORD) {
            toFound = true;
            lexer.advance();

            // Parse destination
            if (lexer.lookAhead().type() != TokenType.GRAPH_POINT) {
                throw new ParseException("Expected graph point after TO");
            }
            GraphPoint destination = parser.parseGraphPoint(lexer.lookAhead().value());
            lexer.advance();

            // Set destination for locomotive
            actions.setDestination(locId, destination);

            // Optional FACING <graphpoint>
            if (lexer.lookAhead().type() == TokenType.FACING_KEYWORD) {
                lexer.advance();
                if (lexer.lookAhead().type() != TokenType.GRAPH_POINT) {
                    throw new ParseException("Expected graph point after FACING");
                }
                GraphPoint facing = parser.parseGraphPoint(lexer.lookAhead().value());
                lexer.advance();

                actions.setFacingDirection(locId, facing);
            }

            // Optional USING <optimization>
            if (lexer.lookAhead().type() == TokenType.USING_KEYWORD) {
                lexer.advance();
                if (lexer.lookAhead().type() != TokenType.OPTIMIZATION) {
                    throw new ParseException("Expected optimization after USING");
                }
                RoutingOptimization optimization = parser.parseOptimization(lexer.lookAhead().value());
                lexer.advance();

                actions.setOptimization(locId, optimization);
            }
        }

        if (!toFound) {
            throw new ParseException("Expected at least one TO statement");
        }

        // DRIVE keyword
        lexer.expect(TokenType.DRIVE_COMMAND);

        // Optional CONSIDER (ELECTRIFICATION | HEIGHT)
        if (lexer.lookAhead().type() == TokenType.CONSIDER_KEYWORD) {
            lexer.advance();

            if (lexer.lookAhead().type() == TokenType.ELECTRIFICATION_KEYWORD) {
                lexer.advance();
                actions.setElectrificationConsideration(true);
            } else if (lexer.lookAhead().type() == TokenType.HEIGHT_KEYWORD) {
                lexer.advance();
                actions.setHeightConsideration(true);
            } else {
                throw new ParseException("Expected ELECTRIFICATION or HEIGHT after CONSIDER");
            }
        }

        // Execute the route generation
        actions.generateAndDriveRoute();
    }

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super(message);
        }
    }
}