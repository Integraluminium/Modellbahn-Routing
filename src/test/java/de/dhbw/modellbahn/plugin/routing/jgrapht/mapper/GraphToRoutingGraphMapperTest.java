package de.dhbw.modellbahn.plugin.routing.jgrapht.mapper;

import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.plugin.DomainGraphFactory;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class GraphToRoutingGraphMapperTest {
    private static Graph graph;

    private GraphToRoutingGraphMapper mapper;

    @BeforeAll
    static void beforeAll() {
        graph = DomainGraphFactory.createTestGraph();
    }

    @BeforeEach
    void setUp() {
        mapper = new GraphToRoutingGraphMapper();
    }

    @Test
    void testElectrified() {
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> jGraph = mapper.mapGraphToJGraphT(graph, true, false, new ArrayList<>());
        assertThat(graph.getAllVertices().size()).isEqualTo(7);
        assertThat(jGraph.vertexSet().size()).isEqualTo(14);

        Optional<DirectedNode> nodeA = jGraph.vertexSet().stream().filter(vertex -> vertex.getPoint().getName().equals("A")).findFirst();
        assertThat(nodeA.isPresent()).isTrue();

        boolean isEInEdges = jGraph.edgesOf(nodeA.get()).stream().anyMatch(edge -> jGraph.getEdgeTarget(edge).getNodeName().equals("E"));
        assertThat(isEInEdges).isFalse();

        // check graph is not altered
        Graph graph1 = DomainGraphFactory.createTestGraph();
        assertThat(graph1).usingRecursiveComparison().isEqualTo(graph);
    }

    @Test
    void testBlockedPoints() {
        List<GraphPoint> blockedPoints = new ArrayList<>();

        GraphPoint blockedPoint = GraphPoint.of("C");
        blockedPoints.add(blockedPoint);
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> jGraph = mapper.mapGraphToJGraphT(graph, false, false, blockedPoints);

        boolean exists = jGraph.vertexSet().stream().anyMatch(vertex -> vertex.getPoint().equals(blockedPoint));
        assertThat(exists).isFalse();

        boolean existsInEdges = jGraph.edgeSet().stream().anyMatch(edge -> jGraph.getEdgeSource(edge).getPoint().equals(blockedPoint) || jGraph.getEdgeTarget(edge).getPoint().equals(blockedPoint));
        assertThat(existsInEdges).isFalse();

        // check graph is not altered
        Graph graph1 = DomainGraphFactory.createTestGraph();
        assertThat(graph1).usingRecursiveComparison().isEqualTo(graph);
    }

    @Test
    void testBlockedAllPoints() {
        List<GraphPoint> blockedPoints = graph.getAllVertices().stream().toList();

        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> jGraph = mapper.mapGraphToJGraphT(graph, false, false, blockedPoints);

        assertThat(jGraph.vertexSet().isEmpty()).isTrue();

        // check graph is not altered
        Graph graph1 = DomainGraphFactory.createTestGraph();
        assertThat(graph1).usingRecursiveComparison().isEqualTo(graph);
    }

    @Test
    void testBlockedAllPointsBesidesOne() {
        List<GraphPoint> blockedPoints = new ArrayList<>(graph.getAllVertices());

        GraphPoint notBlockedPoint = blockedPoints.removeFirst();
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> jGraph = mapper.mapGraphToJGraphT(graph, false, false, blockedPoints);

        assertThat(jGraph.vertexSet().size()).isEqualTo(2);
        assertThat(jGraph.vertexSet().stream().findFirst().get().getPoint()).isEqualTo(notBlockedPoint);

        assertThat(jGraph.edgeSet().size()).isEqualTo(0);

        // check graph is not altered
        Graph graph1 = DomainGraphFactory.createTestGraph();
        assertThat(graph1).usingRecursiveComparison().isEqualTo(graph);
    }
}