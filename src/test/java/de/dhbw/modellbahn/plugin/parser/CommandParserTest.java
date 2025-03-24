package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.GraphVisualisation;
import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.application.repositories.LocomotiveRepository;
import de.dhbw.modellbahn.application.repositories.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.application.routing.building.RoutingOptimization;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Distance;
import de.dhbw.modellbahn.domain.graph.nodes.attributes.Height;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.parser.CommandParser;
import de.dhbw.modellbahn.parser.DomainObjectParser;
import de.dhbw.modellbahn.parser.ParseException;
import de.dhbw.modellbahn.parser.instructions.*;
import de.dhbw.modellbahn.parser.lexer.Lexer;
import de.dhbw.modellbahn.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
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
        DomainObjectParser domainObjectParser = new DomainObjectParser(graph, repository);
        parser = new CommandParser(lexer, domainObjectParser);
    }

    @Test
    void testBasicCommand() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse("NEW ROUTE ADD 123 TO stationA DRIVE");

        assertEquals(5, instructions.size());
        assertInstanceOf(NewRouteInstr.class, instructions.removeFirst());
        assertInstanceOf(AddLocomotiveToRoutingInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(GenerateRouteInstr.class, instructions.get(2));
        assertInstanceOf(DriveInstr.class, instructions.get(3));

        AddLocomotiveToRoutingInstr addInstr = (AddLocomotiveToRoutingInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());

        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());
    }

    @Test
    void testFullCommand() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse(
                "NEW ROUTE ADD 456 AT stationA FACING stationB TO stationB FACING junctionC USING TIME CONSIDER ELECTRIFICATION");

        assertEquals(9, instructions.size());
        Instruction newRouteInstr = instructions.removeFirst();
        Instruction addLocomotiveToRoutingInstr = instructions.removeFirst();
        Instruction modifyLocPosInstr = instructions.removeFirst();
        Instruction modifyLocFacingInstr = instructions.removeFirst();
        Instruction setDestinationInstr = instructions.removeFirst();
        Instruction setFacingDirectionForDestinationInstr = instructions.removeFirst();
        Instruction setOptimizationInstr = instructions.removeFirst();
        Instruction considerElectrificationInstr = instructions.removeFirst();
        Instruction routeGenerationInstr = instructions.removeFirst();

        assertSame(instructions.size(), 0);

        assertInstanceOf(NewRouteInstr.class, newRouteInstr);
        assertInstanceOf(AddLocomotiveToRoutingInstr.class, addLocomotiveToRoutingInstr);
        assertInstanceOf(ModifyLocPosInstr.class, modifyLocPosInstr);
        assertInstanceOf(ModifyLocFacingInstr.class, modifyLocFacingInstr);
        assertInstanceOf(SetDestinationInstr.class, setDestinationInstr);
        assertInstanceOf(SetFacingDirectionForDestinationInstr.class, setFacingDirectionForDestinationInstr);
        assertInstanceOf(SetOptimizationInstr.class, setOptimizationInstr);
        assertInstanceOf(ConsiderElectrificationInstr.class, considerElectrificationInstr);
        assertInstanceOf(GenerateRouteInstr.class, routeGenerationInstr);


        AddLocomotiveToRoutingInstr addInstr = (AddLocomotiveToRoutingInstr) addLocomotiveToRoutingInstr;
        assertEquals(new LocId(LOC_ID2), addInstr.locId());

        ModifyLocPosInstr modInstr = (ModifyLocPosInstr) modifyLocPosInstr;
        assertEquals(gp_statA, modInstr.destination());

        ModifyLocFacingInstr locFacingInstr = (ModifyLocFacingInstr) modifyLocFacingInstr;
        assertEquals(gp_statB, locFacingInstr.facingPoint());

        SetDestinationInstr destInstr = (SetDestinationInstr) setDestinationInstr;
        assertEquals(gp_statB, destInstr.destination());

        SetFacingDirectionForDestinationInstr faceInstr = (SetFacingDirectionForDestinationInstr) setFacingDirectionForDestinationInstr;
        assertEquals(gp_junC, faceInstr.facing());

        SetOptimizationInstr optInstr = (SetOptimizationInstr) setOptimizationInstr;
        assertEquals(RoutingOptimization.TIME, optInstr.optimization());

        ConsiderElectrificationInstr elecInstr = (ConsiderElectrificationInstr) considerElectrificationInstr;
        assertTrue(elecInstr.toConsider());
    }

    @Test
    void testMultipleToStatements() throws LexerException, ParseException {
        assertThrows(ParseException.class, () -> {
            parser.parse("NEW ROUTE ADD 123 TO stationA TO stationB DRIVE");
        });
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
            parser.parse("NEW ROUTE ADD 999 TO stationA DRIVE");
        });

        assertTrue(exception.getMessage().contains("unknown"));
    }

    @Test
    void testUnknownGraphPoint() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parse("NEW ROUTE ADD 123 TO unknownStation DRIVE");
        });

        assertTrue(exception.getMessage().contains("not in the graph"));
    }

    @Test
    void testConsiderHeight() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse("NEW ROUTE ADD 123 TO stationA CONSIDER HEIGHT DRIVE");

        assertEquals(6, instructions.size());

        Instruction newRouteInstr = instructions.removeFirst();
        Instruction addLocomotiveToRoutingInstr = instructions.removeFirst();
        Instruction setDestinationInstr = instructions.removeFirst();
        Instruction considerHeightInstr = instructions.removeFirst();
        Instruction routeGenerationInstr = instructions.removeFirst();
        Instruction driveInstr = instructions.removeFirst();

        assertSame(instructions.size(), 0);

        assertInstanceOf(NewRouteInstr.class, newRouteInstr);
        assertInstanceOf(AddLocomotiveToRoutingInstr.class, addLocomotiveToRoutingInstr);
        assertInstanceOf(SetDestinationInstr.class, setDestinationInstr);
        assertInstanceOf(ConsiderHeightInstr.class, considerHeightInstr);
        assertInstanceOf(GenerateRouteInstr.class, routeGenerationInstr);
        assertInstanceOf(DriveInstr.class, driveInstr);


        AddLocomotiveToRoutingInstr addInstr = (AddLocomotiveToRoutingInstr) addLocomotiveToRoutingInstr;
        assertEquals(new LocId(LOC_ID1), addInstr.locId());


        SetDestinationInstr destInstr = (SetDestinationInstr) setDestinationInstr;
        assertEquals(gp_statA, destInstr.destination());

        ConsiderHeightInstr heightInstr = (ConsiderHeightInstr) considerHeightInstr;
        assertTrue(heightInstr.toConsider());
    }

    @Test
    void testConsiderElectrification() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse("NEW ROUTE ADD 123 TO stationA CONSIDER ELECTRIFICATION DRIVE");

        assertEquals(6, instructions.size());

        Instruction newRouteInstr = instructions.removeFirst();
        assertInstanceOf(NewRouteInstr.class, newRouteInstr);
        assertInstanceOf(AddLocomotiveToRoutingInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(ConsiderElectrificationInstr.class, instructions.get(2));
        assertInstanceOf(GenerateRouteInstr.class, instructions.get(3));


        AddLocomotiveToRoutingInstr addInstr = (AddLocomotiveToRoutingInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());

        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());

        ConsiderElectrificationInstr elecInstr = (ConsiderElectrificationInstr) instructions.get(2);
        assertTrue(elecInstr.toConsider());

    }

    @Test
    void testMultipleCommands() throws LexerException, ParseException {
        List<Instruction> instructions = parser.parse("NEW ROUTE ADD 123 TO stationA LIST ROUTE NEW ROUTE ADD 456 TO stationB DRIVE");


        assertEquals(10, instructions.size());
        Instruction newRouteInstr = instructions.removeFirst();
        assertInstanceOf(NewRouteInstr.class, newRouteInstr);

        // First command
        assertInstanceOf(AddLocomotiveToRoutingInstr.class, instructions.get(0));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(1));
        assertInstanceOf(GenerateRouteInstr.class, instructions.remove(2));

        // Second command
        assertInstanceOf(ListRouteInstr.class, instructions.get(2));

        // Third command
        assertInstanceOf(NewRouteInstr.class, instructions.remove(3));
        assertInstanceOf(AddLocomotiveToRoutingInstr.class, instructions.get(3));
        assertInstanceOf(SetDestinationInstr.class, instructions.get(4));
        assertInstanceOf(GenerateRouteInstr.class, instructions.get(5));
        assertInstanceOf(DriveInstr.class, instructions.get(6));


        AddLocomotiveToRoutingInstr addInstr = (AddLocomotiveToRoutingInstr) instructions.get(0);
        assertEquals(new LocId(LOC_ID1), addInstr.locId());
        SetDestinationInstr destInstr = (SetDestinationInstr) instructions.get(1);
        assertEquals(gp_statA, destInstr.destination());

        AddLocomotiveToRoutingInstr addInstr2 = (AddLocomotiveToRoutingInstr) instructions.get(3);
        assertEquals(new LocId(LOC_ID2), addInstr2.locId());
        SetDestinationInstr destInstr2 = (SetDestinationInstr) instructions.get(4);
        assertEquals(gp_statB, destInstr2.destination());
    }

    @Test
    void testMultiLine() throws LexerException, ParseException {
        String command = """
                NEW ROUTE
                    ADD 123 AT stationA FACING stationB TO junctionC FACING junctionD USING DISTANCE
                    ADD 456
                    CONSIDER ELECTRIFICATION HEIGHT
                    WITH Dijkstra
                
                """;
        parser.parse(command);
    }

    @Test
    void testMultiLineAndMultiStatements() throws LexerException, ParseException {
        String command = """
                // Example MobaScript file
                SYSTEM START
                
                LIST GRAPHPOINTS
                LIST LOCOMOTIVES
                
                MODIFY 123 POSITION stationA FACING stationB
                MODIFY 123 SPEED 0
                
                NEW ROUTE
                    ADD 123 AT stationA FACING stationB TO junctionC FACING junctionD USING DISTANCE
                    ADD 456 TO stationA USING DISTANCE
                    CONSIDER ELECTRIFICATION HEIGHT
                    WITH Dijkstra
                
                // should show the route for the locomotives
                LIST ROUTE
                DRIVE
                
                LIST GRAPHPOINTS
                
                SYSTEM STOP
                """;
        parser.parse(command);
    }

    @Test
    void testComments() throws ParseException, LexerException {
        parser.parse("// This is a comment");
        parser.parse("NEW ROUTE ADD 123 TO stationA // This is a comment");
        parser.parse("# This is a comment");
    }
}