package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.GraphVisualisation;
import de.dhbw.modellbahn.adapter.locomotive.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.application.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Distance;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Height;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommandParserTest {

    private static final int LOC_ID1 = 123;
    private static final int LOC_ID2 = 456;
    private static Graph graph;
    private static LocomotiveRepository repository;
    private static GraphPoint gp_statA, gp1, gp_statB, gp_junC, gp_junD;
    private CommandParser parser;
    private Lexer lexer;

    @BeforeAll
    static void beforeAll() {
        graph = new Graph();
        repository = new LocomotiveRepositoryImpl(new MockedConfigReader(), new MockedLockCalls());

        gp_statA = GraphPoint.of("stationA");
        gp_statB = GraphPoint.of("stationB");
        gp_junC = GraphPoint.of("junctionC");
        gp_junD = GraphPoint.of("junctionD");

        // Add test data
        repository.addLocomotive(new MockedLocomotive(LOC_ID1, gp_junD, gp_statB));
        repository.addLocomotive(new MockedLocomotive(LOC_ID2, gp_junD, gp_statB));

        Distance distance = new Distance(LOC_ID1);
        Height height = new Height(0);

        graph.addEdge(gp_statA, gp_statB, distance, height, true);
        graph.addEdge(gp_statA, gp_junC, distance, height, true);
        graph.addEdge(gp_statB, gp_junD, distance, height, true);
        graph.addEdge(gp_junC, gp_junD, distance, height, true);
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
    void testBasicCommand() throws LexerException, ParseException {
        parser.parse("ADD 123 TO stationA DRIVE");
        List<Instruction> instructions = parser.parse("ADD 123 TO stationA DRIVE");

        assertEquals(3, instructions.size());
        assertInstanceOf(AddLocomotiveInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(DriveInstr.class, instructions.get(2));

        AddLocomotiveInstr addInstr = (AddLocomotiveInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());

        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());
    }

    @Test
    void testFullCommand() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse(
                "ADD 456 AT stationA TO stationB FACING junctionC USING TIME DRIVE CONSIDER ELECTRIFICATION");

        assertEquals(7, instructions.size());
        assertInstanceOf(AddLocomotiveInstr.class, instructions.get(0));
        assertInstanceOf(ModifyLocPosInstr.class, instructions.get(1));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(2));
        assertInstanceOf(SetFacingDirectionInstr.class, instructions.get(3));
        assertInstanceOf(SetOptimizationInstr.class, instructions.get(4));
        assertInstanceOf(ConsiderElectrificationInstr.class, instructions.get(5));
        assertInstanceOf(DriveInstr.class, instructions.get(6));


        AddLocomotiveInstr addInstr = (AddLocomotiveInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID2), addInstr.locId());

        ModifyLocPosInstr modInstr = (ModifyLocPosInstr) instructions.get(1);
        assertEquals(gp_statA, modInstr.destination());

        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(2);
        assertEquals(gp_statB, destInstr.destination());

        SetFacingDirectionInstr faceInstr = (SetFacingDirectionInstr) instructions.get(3);
        assertEquals(gp_junC, faceInstr.facing());

        SetOptimizationInstr optInstr = (SetOptimizationInstr) instructions.get(4);
        assertEquals(RoutingOptimization.TIME, optInstr.optimization());

        ConsiderElectrificationInstr elecInstr = (ConsiderElectrificationInstr) instructions.get(5);
        assertTrue(elecInstr.toConsider());
    }

    @Test
    void testMultipleToStatements() throws LexerException, ParseException {
        Exception exception = assertThrows(ParseException.class, () -> {
            parser.parse("ADD 123 TO stationA TO stationB DRIVE");
        });
        assertTrue(exception.getMessage().contains("Expected DRIVE_COMMAND but got TO_KEYWORD"));
    }

    @Test
    @Disabled
        // cannot be checked with syntactic analysis
    void testMissingTo() {
        Exception exception = assertThrows(ParseException.class, () -> {
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
    void testConsiderHeight() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse("ADD 123 TO stationA DRIVE CONSIDER HEIGHT");

        assertEquals(4, instructions.size());

        assertInstanceOf(AddLocomotiveInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(ConsiderHeightInstr.class, instructions.get(2));
        assertInstanceOf(DriveInstr.class, instructions.get(3));


        AddLocomotiveInstr addInstr = (AddLocomotiveInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());


        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());

        ConsiderHeightInstr heightInstr = (ConsiderHeightInstr) instructions.get(2);
        assertTrue(heightInstr.toConsider());
    }

    @Test
    void testConsiderElectrification() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse("ADD 123 TO stationA DRIVE CONSIDER ELECTRIFICATION");

        assertEquals(4, instructions.size());

        assertInstanceOf(AddLocomotiveInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(ConsiderElectrificationInstr.class, instructions.get(2));
        assertInstanceOf(DriveInstr.class, instructions.get(3));


        AddLocomotiveInstr addInstr = (AddLocomotiveInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());

        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());

        ConsiderElectrificationInstr elecInstr = (ConsiderElectrificationInstr) instructions.get(2);
        assertTrue(elecInstr.toConsider());

    }

    @Test
    void testMultipleCommands() throws LexerException, ParseException {
        parser.parse("ADD 123 TO stationA DRIVE ADD 456 TO stationB DRIVE");

        List<Instruction> instructions = parser.parse(
                "ADD 123 TO stationA DRIVE ADD 456 TO stationB DRIVE");

        assertEquals(6, instructions.size());
        // First command
        assertInstanceOf(AddLocomotiveInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(DriveInstr.class, instructions.get(2));
        // Second command
        assertInstanceOf(AddLocomotiveInstr.class, instructions.get(3));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(4));
        assertInstanceOf(DriveInstr.class, instructions.get(5));

        AddLocomotiveInstr addInstr = (AddLocomotiveInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());
        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());

        AddLocomotiveInstr addInstr2 = (AddLocomotiveInstr) instructions.get(3);
        assertEquals(new LocId(LOC_ID2), addInstr2.locId());
        SetDestinationInstr destInstr2 = (SetDestinationInstr) instructions.get(4);
        assertEquals(gp_statB, destInstr2.destination());


    }
}