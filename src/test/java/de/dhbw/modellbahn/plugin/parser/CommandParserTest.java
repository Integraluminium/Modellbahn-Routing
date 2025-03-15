package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.GraphVisualisation;
import de.dhbw.modellbahn.adapter.locomotive.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Height;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandParserTest {

    private static Graph graph;
    private static LocomotiveRepository repository;
    private CommandParser parser;
    private Lexer lexer;

    @BeforeAll
    static void beforeAll() {
        graph = new Graph();
        repository = new LocomotiveRepositoryImpl(new MockedConfigReader(), new MockedLockCalls());

        GraphPoint gp = GraphPoint.of("stationA");
        GraphPoint gp2 = GraphPoint.of("stationB");
        GraphPoint gp3 = GraphPoint.of("junctionC");
        GraphPoint gp4 = GraphPoint.of("junctionD");

        // Add test data
        repository.addLocomotive(new MockedLocomotive(123, gp4, gp2));
        repository.addLocomotive(new MockedLocomotive(456, gp4, gp2));

        Distance distance = new Distance(123);
        Height height = new Height(0);

        graph.addEdge(gp, gp2, distance, height, true);
        graph.addEdge(gp, gp3, distance, height, true);
        graph.addEdge(gp2, gp4, distance, height, true);
        graph.addEdge(gp3, gp4, distance, height, true);
    }

    public static void main(String[] args) {
        beforeAll();
        GraphVisualisation.showNormalGraph(graph);
    }

    @BeforeEach
    void setUp() {
        lexer = new Lexer();
        parser = new CommandParser(lexer, graph, repository);
    }

    @Test
    void testBasicCommand() throws LexerException, CommandParser.ParseException {
        parser.parse("ADD 123 TO stationA DRIVE");
    }

    @Test
    void testFullCommand() throws LexerException, CommandParser.ParseException {
        parser.parse("ADD 456 AT stationA TO stationB FACING junctionC USING TIME DRIVE CONSIDER ELECTRIFICATION");
    }

    @Test
    void testMultipleToStatements() throws LexerException, CommandParser.ParseException {
        parser.parse("ADD 123 TO stationA TO stationB USING DISTANCE TO stationA DRIVE");
    }

    @Test
    void testMissingTo() {
        Exception exception = assertThrows(CommandParser.ParseException.class, () -> {
            parser.parse("ADD 123 DRIVE");
        });

        assertTrue(exception.getMessage().contains("Expected at least one TO statement"));
    }

    @Test
    void testUnknownLocId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("ADD 999 TO stationA DRIVE");
        });

        assertTrue(exception.getMessage().contains("unknown"));
    }

    @Test
    void testUnknownGraphPoint() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("ADD 123 TO unknownStation DRIVE");
        });

        assertTrue(exception.getMessage().contains("not in the graph"));
    }

    @Test
    void testConsiderHeight() throws LexerException, CommandParser.ParseException {
        parser.parse("ADD 123 TO stationA DRIVE CONSIDER HEIGHT");
    }

    @Test
    void testConsiderElectrification() throws LexerException, CommandParser.ParseException {
        parser.parse("ADD 123 TO stationA DRIVE CONSIDER ELECTRIFICATION");
    }

    @Test
    void testMultipleCommands() throws LexerException, CommandParser.ParseException {
        parser.parse("ADD 123 TO stationA DRIVE ADD 456 TO stationB DRIVE");
    }
}