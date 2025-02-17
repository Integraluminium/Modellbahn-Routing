package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.routing.GraphMapper;
import de.dhbw.modellbahn.adapter.track_generation.GraphGenerator;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.jgrapht.graph.DefaultWeightedEdge;


public class GraphVisualisation {
    public static void main(String[] args) {

        System.out.println(java.awt.GraphicsEnvironment.isHeadless());

        // Arrange
        GraphMapper mapper = new GraphMapper();

        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);

        Graph graph = generator.generateGraph();
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> actualGraph = mapper.mapGraph(graph);


        showRoutingGraph(actualGraph);
    }

    static void showRoutingGraph(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph) {
        org.graphstream.graph.Graph graphStreamGraph = new org.graphstream.graph.implementations.SingleGraph("Routing Graph");

        for (DirectedNode node : graph.vertexSet()) {
            Node nodejs = graphStreamGraph.addNode(node.getNodeName());
            nodejs.setAttribute("ui.label", node.getNodeName());
            nodejs.setAttribute("ui.style", "text-size: 30px; text-color: red;");

        }

        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            DirectedNode source = graph.getEdgeSource(edge);
            DirectedNode target = graph.getEdgeTarget(edge);
            Edge msedge = graphStreamGraph.addEdge(source.getNodeName() + " => " + target.getNodeName(), source.getNodeName(), target.getNodeName(), true);
            msedge.setAttribute("ui.style", "arrow-size: 8px;");
            msedge.setAttribute("ui.arrow", "true");

        }

        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph.display();

//        Viewer viewer = graphStreamGraph.display();
//        viewer.enableAutoLayout();

    }

}
