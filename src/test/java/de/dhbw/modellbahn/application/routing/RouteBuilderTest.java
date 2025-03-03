package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.adapter.routing.GraphMapper;
import de.dhbw.modellbahn.adapter.track_generation.GraphGenerator;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import org.junit.jupiter.api.BeforeEach;

class RouteBuilderTest {
    private RouteBuilder routeBuilder;

    @BeforeEach
    void setUp() {
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);
        Graph graph = generator.generateGraph();
        GraphMapper graphMapper = new GraphMapper();


        routeBuilder = new RouteBuilder(graph, monoTrainRouting, graphMapper);
    }

}