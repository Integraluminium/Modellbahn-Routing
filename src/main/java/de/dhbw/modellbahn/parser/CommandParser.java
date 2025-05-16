package de.dhbw.modellbahn.parser;

import de.dhbw.modellbahn.application.routing.building.RoutingAlgorithm;
import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.TrackContact;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.parser.instructions.*;
import de.dhbw.modellbahn.parser.lexer.Lexer;
import de.dhbw.modellbahn.parser.lexer.LexerException;
import de.dhbw.modellbahn.parser.lexer.Token;
import de.dhbw.modellbahn.parser.lexer.TokenType;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommandParser {
    private final Lexer lexer;
    private final DomainObjectParser parser;

    private List<Instruction> instructions;

    public CommandParser(Lexer lexer, DomainObjectParser domainObjectParser) {
        this.lexer = lexer;
        this.parser = domainObjectParser;
    }

    private File parseFilePath(final Token filePath, String suffix) throws ParseException {
        Path path = Paths.get(filePath.value());
        if (!path.toString().endsWith(suffix)) {
            throw new ParseException("Expected file path with" + suffix + "extension but got: " + filePath);
        }
        File file = path.toFile();
        if (!file.exists()) {
            throw new ParseException("File " + file.getAbsolutePath() + " does not exist");
        }
        return file;
    }

    public List<Instruction> parse(String input) throws LexerException, ParseException {
        instructions = new ArrayList<>();
        lexer.init(input);

        while (lexer.lookAhead().type() != TokenType.EOF) {
            parseStatement();
        }
        return instructions; // mutability is fine here, as new parsing is creating a new ArrayList
    }

    private void parseStatement() throws LexerException, ParseException {
        // <command> ::= NEW ROUTE <route_command> | MODIFY <loc_id> <modify_command> | LIST <list_command> | SYSTEM <system_command> | DRIVE
        Token token = lexer.lookAhead();
        lexer.advance();

        switch (token.type()) {
            case NEW_KEYWORD -> {
                lexer.expect(TokenType.ROUTE_KEYWORD);
                parseRouteCommand();
            }
            case RUN_KEYWORD -> parseRunCommand();
            case MODIFY_KEYWORD -> parseModificationCommand();
            case LIST_KEYWORD -> parseInformationCommand();
            case SYSTEM_KEYWORD -> parseSystemInformation();
            case DRIVE_COMMAND -> instructions.add(new DriveInstr());
            case REMOVE_KEYWORD -> {
                // moves locomotive to NotOnTrack
                LocId locId = parseLocId();
                instructions.add(new RemoveLocomotiveFromTrackInstr(locId));
            }
            case AWAIT_KEYWORD -> parseAwaitExpression();
            default -> throw new ParseException("Expected statement but got: " + token);
        }
    }

    private void parseRunCommand() throws ParseException, LexerException {
        Token filePath = lexer.lookAhead();
        lexer.advance();
        if (filePath.type() != TokenType.FILE_PATH) {
            throw new ParseException("Expected file path but got: " + filePath);
        }
        File file = parseFilePath(filePath, ".moba");

        instructions.add(new RunScriptInstr(file, this));
    }

    private void parseAwaitExpression() throws ParseException, LexerException {
        GraphPoint awaitedGraphPoint = parseGraphPoint();
        if (!(awaitedGraphPoint instanceof TrackContact point)) {
            throw new ParseException("Expected track contact but got: " + awaitedGraphPoint);
        }

        lexer.expect(TokenType.TIMEOUT_KEYWORD);
        int timeout = parseNumber();


        instructions.add(new AwaitTrackContact(point, timeout));
    }

    private void parseRouteCommand() throws LexerException, ParseException {
        // <route_command> ::= <add_command>* (CONSIDER <consider_values>+)? (WITH <routing_algorithm>)?

        instructions.add(new NewRouteInstr());

        // Parse multiple ADD commands
        lexer.expect(TokenType.ADD_KEYWORD);
        parseAddCommand();
        while (lexer.lookAhead().type() == TokenType.ADD_KEYWORD) {
            lexer.advance();
            parseAddCommand();
        }

        // Optional CONSIDER clause
        if (lexer.lookAhead().type() == TokenType.CONSIDER_KEYWORD) {
            lexer.advance();
            parseConsiderValues();
        }

        // Optional WITH clause for routing algorithm
        if (lexer.lookAhead().type() == TokenType.WITH_KEYWORD) {
            lexer.advance();
            parseRoutingAlgorithm();
        }

        instructions.add(new GenerateRouteInstr());
    }

    private void parseAddCommand() throws LexerException, ParseException {
        // <add_command> ::= "ADD" <loc_id> ["AT" <graph_point> "FACING" <graph_point>]? ["TO" <graph_point> ["FACING" <graph_point>]?]? ["USING" <optimization>]?
        LocId locId = parseLocId();
        instructions.add(new AddLocomotiveToRoutingInstr(locId));

        // Optional AT <graphpoint> FACING <graphpoint>
        if (lexer.lookAhead().type() == TokenType.AT_KEYWORD) {
            lexer.advance();
            parseAtStartPosition(locId);
        }

        // Optional TO <graphpoint> [FACING <graphpoint>]?
        if (lexer.lookAhead().type() == TokenType.TO_KEYWORD) {
            lexer.advance();
            parseDestination(locId);
        }

        // Optional USING <optimization>
        if (lexer.lookAhead().type() == TokenType.USING_KEYWORD) {
            lexer.advance();
            parseOptimization(locId);
        }
    }

    private void parseConsiderValues() throws LexerException, ParseException {
        // <consider_values> ::= "ELECTRIFICATION" | "HEIGHT"
        // Parse at least one value, potentially multiple
        boolean parsedValue = false;

        while (true) {
            Token token = lexer.lookAhead();
            if (!(token.type() == TokenType.ELECTRIFICATION_KEYWORD || token.type() == TokenType.HEIGHT_KEYWORD)) {
                break;
            }
            parsedValue = true;

            if (token.type() == TokenType.ELECTRIFICATION_KEYWORD) {
                lexer.advance();
                instructions.add(new ConsiderElectrificationInstr(true));
            } else {
                lexer.advance();
                instructions.add(new ConsiderHeightInstr(true));
            }
        }

        if (!parsedValue) {
            throw new ParseException("Expected ELECTRIFICATION or HEIGHT after CONSIDER");
        }
    }

    private void parseRoutingAlgorithm() throws LexerException, ParseException {
        // <routing_algorithm> ::= "Dijkstra" | "BellmanFord"
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.ALGORITHM) {
            throw new ParseException("Expected routing algorithm (Dijkstra or Bellman_Ford) but got: " + token);
        }
        RoutingAlgorithm algorithm = parser.parseRoutingAlgorithm(token.value());
        lexer.advance();

        // Add instruction for setting algorithm
        instructions.add(new SetRoutingAlgorithmInstr(algorithm));
    }

    private void parseSystemInformation() throws LexerException, ParseException {
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

    private void parseInformationCommand() throws LexerException, ParseException {
        Token token = lexer.lookAhead();

        if (token.type() == TokenType.LOCOMOTIVES_KEYWORD) {
            lexer.advance();
            instructions.add(new ListLocomotivesInstr());
        } else if (token.type() == TokenType.GRAPHPOINTS_KEYWORD) {
            lexer.advance();
            instructions.add(new ListGraphPointsInstr());
        } else if (token.type() == TokenType.ROUTE_KEYWORD) {
            lexer.advance();
            instructions.add(new ListRouteInstr());
        } else {
            throw new ParseException("Expected LOCOMOTIVES, GRAPHPOINTS, or ROUTE after LIST but got: " + token);
        }
    }

    private void parseModificationCommand() throws LexerException, ParseException {
        // <modify_command> ::= <loc_id> <modify_locomotive_command>
        //                   | AUTOMATIC ADD LOCOMOTIVES TO ROUTE <boolean>
        //
        if (lexer.lookAhead().type() == TokenType.AUTOMATIC_KEYWORD) {
            lexer.advance();
            lexer.expect(TokenType.ADD_KEYWORD);
            lexer.expect(TokenType.LOCOMOTIVES_KEYWORD);
            lexer.expect(TokenType.TO_KEYWORD);
            lexer.expect(TokenType.ROUTE_KEYWORD);
            boolean value = parseBoolean();
            instructions.add(new SetAutomaticAddLocomotivesInstr(value));
        } else {
            LocId locId = parseLocId();
            parseModifyLocomotiveCommand(locId);
        }
    }

    private void parseModifyLocomotiveCommand(final LocId locId) throws LexerException, ParseException {
        // <modify_locomotive_command> ::= "TOGGLE" "DIRECTION"
        //                    | "POSITION" <graph_point> "FACING" <graph_point>
        //                    | "FACING" <graph_point>
        //                    | "SPEED" <number>

        // Parse modification type
        Token modification_token = lexer.lookAhead();
        lexer.advance();

        if (modification_token.type() == TokenType.TOGGLE_KEYWORD) {
            lexer.expect(TokenType.DIRECTION_KEYWORD);
            instructions.add(new ToggleDirectionInstr(locId));

        } else if (modification_token.type() == TokenType.POSITION_KEYWORD) {
            GraphPoint position = parseGraphPoint();
            instructions.add(new ModifyLocPosInstr(locId, position));

            lexer.expect(TokenType.FACING_KEYWORD);
            GraphPoint facingPoint = parseGraphPoint();
            instructions.add(new ModifyLocFacingInstr(locId, facingPoint));

        } else if (modification_token.type() == TokenType.FACING_KEYWORD) {
            GraphPoint facingPoint = parseGraphPoint();
            instructions.add(new ModifyLocFacingInstr(locId, facingPoint));

        } else if (modification_token.type() == TokenType.SPEED_KEYWORD) {
            Speed speed = new Speed(parseNumber());
            instructions.add(new ModifyLocSpeedInstr(locId, speed));

        } else {
            throw new ParseException("Expected TOGGLE, POSITION, FACING, or SPEED after locomotive ID but got: " + modification_token);
        }
    }

    private void parseDestination(final LocId locId) throws LexerException, ParseException {
        GraphPoint destination = parseGraphPoint();
        instructions.add(new SetDestinationInstr(locId, destination));

        // Optional FACING <graphpoint>
        if (lexer.lookAhead().type() == TokenType.FACING_KEYWORD) {
            lexer.advance();
            GraphPoint facingPoint = parseGraphPoint();
            instructions.add(new SetFacingDirectionForDestinationInstr(locId, facingPoint));
        }
    }

    private void parseOptimization(final LocId locId) throws ParseException, LexerException {
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.OPTIMIZATION) {
            throw new ParseException("Expected optimization Token but got: " + token);
        }
        lexer.advance();
        RoutingOptimization optimization = parser.parseOptimization(token.value());
        instructions.add(new SetOptimizationInstr(locId, optimization));
    }

    private GraphPoint parseGraphPoint() throws LexerException, ParseException {
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.STRING) {
            throw new ParseException("Expected graph point but got: " + token);
        }
        lexer.advance();
        return parser.parseGraphPoint(token.value());
    }

    private LocId parseLocId() throws LexerException, ParseException {
        Token token = lexer.lookAhead();
        if (token.type() != TokenType.NUMBER) {
            throw new ParseException("Expected locomotive ID but got: " + token);
        }
        lexer.advance();
        return parser.parseLocId(token.value());
    }

    private int parseNumber() throws LexerException, ParseException {
        Token token = lexer.lookAhead();
        lexer.advance();
        if (token.type() != TokenType.NUMBER) {
            throw new ParseException("Expected number but got: " + token);
        }
        int number;
        try {
            number = Integer.parseInt(token.value());
        } catch (NumberFormatException e) {
            throw new ParseException("Expected integer value but got: " + token);
        }
        return number;
    }

    private void parseAtStartPosition(LocId locId) throws LexerException, ParseException {
        GraphPoint point = parseGraphPoint();
        instructions.add(new ModifyLocPosInstr(locId, point));

        lexer.expect(TokenType.FACING_KEYWORD);
        GraphPoint facingPoint = parseGraphPoint();
        instructions.add(new ModifyLocFacingInstr(locId, facingPoint));
    }

    private boolean parseBoolean() throws LexerException, ParseException {
        Token token = lexer.lookAhead();
        lexer.advance();
        if (token.type() == TokenType.NUMBER) {
            if (!token.value().equals("0") && !token.value().equals("1")) {
                throw new ParseException("Expected boolean value (0 or 1) but got: " + token);
            }
            return token.value().equals("1");
        } else if (token.type() == TokenType.BOOLEAN) {
            return token.value().equalsIgnoreCase("true");
        } else {
            throw new ParseException("Expected boolean value but got: " + token);
        }

    }
}