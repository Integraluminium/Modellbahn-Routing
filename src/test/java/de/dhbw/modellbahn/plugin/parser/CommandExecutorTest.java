package de.dhbw.modellbahn.plugin.parser;

import de.dhbw.modellbahn.adapter.locomotive.LocomotiveRepositoryImpl;
import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedLockCalls;
import de.dhbw.modellbahn.adapter.moba.communication.calls.MockedSystemCalls;
import de.dhbw.modellbahn.application.LocomotiveRepository;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.MockedLocomotive;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import de.dhbw.modellbahn.plugin.parser.lexer.Lexer;
import de.dhbw.modellbahn.plugin.parser.lexer.LexerException;
import de.dhbw.modellbahn.plugin.parser.lexer.instructions.Instruction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static de.dhbw.modellbahn.plugin.DomainGraphFactory.createSmallTestGraph;


class CommandExecutorTest {
    private static final int LOC_ID1 = 123;
    private static final int LOC_ID2 = 456;

    private static Graph graph;
    private static LocomotiveRepository repository;
    private static MockedSystemCalls systemCalls;

    private CommandExecutor executor;
    private CommandParser parser;

    @BeforeAll
    static void beforeAll() {
        graph = createSmallTestGraph();
        repository = new LocomotiveRepositoryImpl(new MockedConfigReader(), new MockedLockCalls());
        repository.addLocomotive(new MockedLocomotive(LOC_ID1, GraphPoint.of("A"), GraphPoint.of("B")));
        repository.addLocomotive(new MockedLocomotive(LOC_ID2, GraphPoint.of("A"), GraphPoint.of("B")));
        systemCalls = new MockedSystemCalls();
    }

    public static void main(String[] args) {
        beforeAll();
    }

    @BeforeEach
    void setUp() {
        parser = new CommandParser(new Lexer(), graph, repository);
        executor = new CommandExecutor(repository, graph, systemCalls, System.out);
    }

    @Test
    void testExecute() throws Exception, LexerException {
        List<Instruction> commands = parser.parse("NEW ROUTE ADD 123 TO C DRIVE");
        executor.execute(commands, true);
    }

}