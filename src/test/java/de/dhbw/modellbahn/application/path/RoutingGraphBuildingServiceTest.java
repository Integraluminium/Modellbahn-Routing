package de.dhbw.modellbahn.application.path;

import de.dhbw.modellbahn.domain.graph2.DirectedNode;
import de.dhbw.modellbahn.domain.graph2.NODE_DIRECTION;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RoutingGraphBuildingServiceTest {
    private static final String inputData = """
            switches:
              A:
                straight: C.straight
                diverging: Z.cn1
                root: B.root
              B:
                straight: C.root
                diverging: C.diverging
                root: A.root
              C:
                straight: A.straight
                diverging: B.diverging
                root: B.straight
            
            endpoints:
              Z:
                cn1: A.diverging
            """;
    private DirectedNode pointAs;
    private DirectedNode pointAw;
    private DirectedNode pointBs;
    private DirectedNode pointBw;
    private DirectedNode pointCs;
    private DirectedNode pointCw;
    private DirectedNode pointZs;
    private DirectedNode pointZw;

    private GraphService graphService;


    @BeforeEach
    void setUp() {
        RoutingGraphBuildingService routingGraphBuildingService = new RoutingGraphBuildingService();

        graphService = routingGraphBuildingService.buildGraph(inputData);

        pointAs = routingGraphBuildingService.getGraphNodeComponent("A", NODE_DIRECTION.S);
        pointAw = routingGraphBuildingService.getGraphNodeComponent("A", NODE_DIRECTION.W);
        pointBs = routingGraphBuildingService.getGraphNodeComponent("B", NODE_DIRECTION.S);
        pointBw = routingGraphBuildingService.getGraphNodeComponent("B", NODE_DIRECTION.W);
        pointCs = routingGraphBuildingService.getGraphNodeComponent("C", NODE_DIRECTION.S);
        pointCw = routingGraphBuildingService.getGraphNodeComponent("C", NODE_DIRECTION.W);
        pointZs = routingGraphBuildingService.getGraphNodeComponent("Z", NODE_DIRECTION.S);
        pointZw = routingGraphBuildingService.getGraphNodeComponent("Z", NODE_DIRECTION.W);
    }

    @Test
    void testBuildingGraph_is_not_null() {
        RoutingGraphBuildingService routingGraphBuildingService = new RoutingGraphBuildingService();
        var graphService = routingGraphBuildingService.buildGraph(inputData);

        assertNotNull(graphService);
    }

    @Test
    void testShortestPath() throws PathNotPossibleException {
        assertNotNull(graphService.findShortestPath(pointAs, pointCs));
        var path = graphService.findShortestPath(pointAs, pointCs);
        assertThat(path).containsExactly(pointAs, pointBw, pointCs);

    }

    @Test
    void testShortestPath2() throws PathNotPossibleException {
        var goal = pointZs;
//        graphService.addStartNode(new StartNode(), pointAs);
        var path = graphService.findShortestPath(pointAs, goal);
        assertThat(path).isNotEmpty();
        assertThat(path).containsExactly(pointAs, pointBw, pointCs, pointBs, pointAw, pointZs);
    }

    @Test
    void testIllegalPath() {
        var goal = pointZw;
        assertThrows(PathNotPossibleException.class, () -> graphService.findShortestPath(pointAs, goal));
    }

    @Test
    void testIllegalPath2() {
        assertThrows(PathNotPossibleException.class, () -> graphService.findShortestPath(pointAw, pointAs));
    }
}