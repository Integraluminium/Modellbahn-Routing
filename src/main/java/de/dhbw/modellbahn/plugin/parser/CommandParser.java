package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.Token;
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
    private final DomainObjectParser parser;

    private List<Instruction> instructions;

    public CommandParser(Lexer lexer, Graph graph, LocomotiveRepository locomotiveRepository) {
        this.lexer = lexer;
        this.parser = new DomainObjectParser(graph, locomotiveRepository);
    }

    public List<Instruction> parse(String input) throws LexerException, ParseException {
        instructions = new ArrayList<>();
        lexer.init(input);

        while (lexer.lookAhead().type() != TokenType.EOF) {
            parseStatement();
        }

        return instructions;
    }

    private void parseStatement() throws LexerException, ParseException {
        // MODIFY <modification> | ADD <routingCommand>

        Token token = lexer.lookAhead();
        if (token.type() == TokenType.ADD_KEYWORD) {
            parseRoutingCommand();
        } else if (token.type() == TokenType.MODIFY_KEYWORD) {
            parseModificationCommand();
        } else if (token.type() == TokenType.LIST_KEYWORD) {
            parseInformationCommand();
        } else if (token.type() == TokenType.SYSTEM_KEYWORD) {
            parseSystemInformation();
        } else {
            throw new LexerException("Expected statement but got: " + token);
        }
    }

    private void parseSystemInformation() throws LexerException, ParseException {
        lexer.expect(TokenType.SYSTEM_KEYWORD);
        Token token = lexer.lookAhead();
        lexer.advance();
        if (token.type() == TokenType.START_KEYWORD) {
            instructions.add(new SystemStateInstr(true));
        } else if (token.type() == TokenType.STOP_KEYWORD) {
            instructions.add(new SystemStateInstr(false));
        } else {
            throw new ParseException("Expected START or STOP Keyword after SYSTEM");
        }
    }

    private void parseModificationCommand() throws LexerException, ParseException {
        lexer.expect(TokenType.MODIFY_KEYWORD);

        // Parse locomotive ID
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.LOC_ID) {
            throw new ParseException("Expected locomotive ID after MODIFY but got: " + token);
        }

        LocId locId = parser.parseLocId(token.value());
        lexer.advance();

        // Parse modification type
        Token modification_token = lexer.lookAhead();

        if (modification_token.type() == TokenType.TOGGLE_KEYWORD) {
            lexer.advance();
            lexer.expect(TokenType.DIRECTION_KEYWORD);
            instructions.add(new ToggleDirectionInstr(locId));
        } else if (modification_token.type() == TokenType.POSITION_KEYWORD) {
            lexer.advance();
            modification_token = lexer.lookAhead();

            if (modification_token.type() != TokenType.GRAPH_POINT) {
                throw new ParseException("Expected graph point for position but got: " + modification_token);
            }

            GraphPoint position = parser.parseGraphPoint(modification_token.value());
            instructions.add(new ModifyLocPosInstr(locId, position));
            lexer.advance();
        } else if (modification_token.type() == TokenType.FACING_KEYWORD) {
            lexer.advance();
            GraphPoint facingPoint = parseGraphPoint();
            instructions.add(new ModifyLocFacingInstr(locId, facingPoint));
        } else if (modification_token.type() == TokenType.SPEED) {
            lexer.advance();
            Token speed_token = lexer.lookAhead();
            lexer.advance();
            if (speed_token.type() != TokenType.LOC_ID) { // LOC_ID is NUMBER
                throw new ParseException("Expected locomotive ID after ADD got: " + token);
            }
            try {
                Speed speed = new Speed(Integer.parseInt(speed_token.value()));
                instructions.add(new ModifyLocSpeedInstr(locId, speed));

            } catch (NumberFormatException e) {
                throw new ParseException("Expected integer value but got: " + speed_token);
            }
        } else {
            throw new ParseException("Expected TOGGLE or POSITION after locomotive ID but got: " + modification_token);
        }
    }

    private void parseInformationCommand() throws LexerException, ParseException {
        lexer.expect(TokenType.LIST_KEYWORD);

        Token token = lexer.lookAhead();

        if (token.type() == TokenType.LOCOMOTIVES_KEYWORD) {
            lexer.advance();
            instructions.add(new ListLocomotivesInstr());
        } else if (token.type() == TokenType.GRAPHPOINTS_KEYWORD) {
            lexer.advance();
            instructions.add(new ListGraphPointsInstr());
        } else {
            throw new ParseException("Expected LOCOMOTIVES or GRAPHPOINTS after LIST but got: " + token);
        }
    }

    private void parseRoutingCommand() throws LexerException, ParseException {
        // ADD <locId> [AT <position>]? [[TO <destination>] [FACING <direction>]?] [USING <optimization>]? DRIVE [CONSIDER <considerations>]?
        lexer.expect(TokenType.ADD_KEYWORD);

        // Parse locomotive ID
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.LOC_ID) {
            throw new ParseException("Expected locomotive ID after ADD got: " + token);
        }
        LocId locId = parser.parseLocId(token.value());
        lexer.advance();
        instructions.add(new AddLocomotiveToRoutingInstr(locId));

        // Optional AT <graphpoint>
        if (lexer.lookAhead().type() == TokenType.AT_KEYWORD) {
            lexer.advance();
            parseAtStartPosition(locId);
        }

        // Optional TO <graphpoint>;
        // At least one TO keyword is required
        // but cannot be checked in syntactic analysis
        if (lexer.lookAhead().type() == TokenType.TO_KEYWORD) {
            lexer.advance();
            parseDestination(locId);
        }

        // DRIVE keyword
        lexer.expect(TokenType.DRIVE_COMMAND);

        // Optional CONSIDER (ELECTRIFICATION | HEIGHT)
        if (lexer.lookAhead().type() == TokenType.CONSIDER_KEYWORD) {
            lexer.advance();
            parseGlobalRoutingModifier();
        }
        // Execute the route generation
        instructions.add(new DriveInstr());
    }

    private void parseGlobalRoutingModifier() throws LexerException, ParseException {
        // (ELECTRIFICATION?  HEIGHT?) | (HEIGHT? ELECTRIFICATION?)
        Token token = lexer.lookAhead();
        if (token.type() == TokenType.ELECTRIFICATION_KEYWORD) {
            lexer.advance();
            instructions.add(new ConsiderElectrificationInstr(true));
            if (lexer.lookAhead().type() == TokenType.HEIGHT_KEYWORD) {
                lexer.advance();
                instructions.add(new ConsiderHeightInstr(true));
            }
        } else if (token.type() == TokenType.HEIGHT_KEYWORD) {
            lexer.advance();
            instructions.add(new ConsiderHeightInstr(true));
            if (lexer.lookAhead().type() == TokenType.ELECTRIFICATION_KEYWORD) {
                lexer.advance();
                instructions.add(new ConsiderElectrificationInstr(true));
            }
        } else {
            throw new ParseException("Expected ELECTRIFICATION or HEIGHT as Routing Modifier, got: " + token);
        }
    }

    private void parseDestination(final LocId locId) throws LexerException, ParseException {
        // <graphpoint> (FACING <graphpoint>)? (USING <optimization>)?
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.GRAPH_POINT) {
            throw new ParseException("Expected graph point as destination. Got: " + token);
        }
        lexer.advance();
        GraphPoint destination = parser.parseGraphPoint(token.value());

        // Set destination for locomotive
        instructions.add(new SetDestinationInstr(locId, destination));

        // Optional FACING <graphpoint>
        if (lexer.lookAhead().type() == TokenType.FACING_KEYWORD) {
            lexer.advance();
            GraphPoint facingPoint = parseGraphPoint();
            instructions.add(new SetFacingDirectionForDestinationInstr(locId, facingPoint));
        }

        // Optional USING (<optimization>)
        if (lexer.lookAhead().type() == TokenType.USING_KEYWORD) {
            lexer.advance();
            parseOptimization(locId);
        }
    }

    private void parseOptimization(final LocId locId) throws ParseException, LexerException {
        // <optimization>:= RoutingOptimization.values
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.OPTIMIZATION) {
            throw new ParseException("Expected optimization Token but got: " + token);
        }
        lexer.advance();
        RoutingOptimization optimization = parser.parseOptimization(token.value());
        instructions.add(new SetOptimizationInstr(locId, optimization));
    }

    private GraphPoint parseGraphPoint() throws LexerException, ParseException {
        // <graphpoint>
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.GRAPH_POINT) {
            throw new ParseException("Expected graph point but got: " + token);
        }
        lexer.advance();
        return parser.parseGraphPoint(token.value());
    }

    private void parseAtStartPosition(LocId locId) throws LexerException, ParseException {
        // <graphpoint>
        Token startPositionToken = lexer.lookAhead();
        if (startPositionToken.type() != TokenType.GRAPH_POINT) {
            throw new ParseException("Expected graph point after AT but got: " + startPositionToken);
        }
        GraphPoint point = parser.parseGraphPoint(startPositionToken.value());
        instructions.add(new ModifyLocPosInstr(locId, point));
        lexer.advance();
    }

}