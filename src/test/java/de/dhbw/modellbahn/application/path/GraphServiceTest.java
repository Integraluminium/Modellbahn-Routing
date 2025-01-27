package de.dhbw.modellbahn.application.path;

import de.dhbw.modellbahn.domain.graph2.DirectedNode;
import de.dhbw.modellbahn.domain.graph2.NODE_DIRECTION;
import de.dhbw.modellbahn.domain.graph2.Route;
import de.dhbw.modellbahn.domain.graph2.SwitchNode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GraphServiceTest {

    @Test
    void testShortestPathIsFound() throws PathNotPossibleException {
        GraphService graphService = new GraphService();

        DirectedNode a = new SwitchNode("A", NODE_DIRECTION.S);
        DirectedNode b = new SwitchNode("B", NODE_DIRECTION.W);
        DirectedNode c = new SwitchNode("C", NODE_DIRECTION.S);

        graphService.addRoute(new Route(a, b, 1.0));
        graphService.addRoute(new Route(b, c, 2.0));
        graphService.addRoute(new Route(a, c, 4.0));


        assertThat(graphService.findShortestPath(a, c)).containsExactly(a, b, c);
    }

    @Test
    void findShortestPath() {
    }
}