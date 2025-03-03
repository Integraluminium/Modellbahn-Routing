package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.moba.communication.ApiService;
import de.dhbw.modellbahn.adapter.moba.communication.calls.TrackComponentCallsAdapter;
import de.dhbw.modellbahn.adapter.routing.GraphMapper;
import de.dhbw.modellbahn.adapter.track_generation.GraphGenerator;
import de.dhbw.modellbahn.application.port.moba.communication.TrackComponentCalls;
import de.dhbw.modellbahn.application.routing.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.Switch;
import de.dhbw.modellbahn.plugin.MockedConfigReader;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashSet;
import java.util.Set;


public class GraphVisualisation {
    public static void main(String[] args) {

        System.out.println(java.awt.GraphicsEnvironment.isHeadless());

        // Arrange
        GraphMapper mapper = new GraphMapper();

        ApiService apiService = new ApiService(0);
        TrackComponentCalls calls = new TrackComponentCallsAdapter(apiService);
        GraphGenerator generator = new GraphGenerator(new MockedConfigReader(), calls);

        Graph graph = generator.generateGraph();
        org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> actualGraph = mapper.mapGraphToJGraphT(graph);


        showNormalGraph(graph);
        showRoutingGraph(actualGraph);
    }

    public static void showNormalGraph(Graph graph) {
        org.graphstream.graph.Graph graphStreamGraph = new org.graphstream.graph.implementations.SingleGraph("Normal Graph");

        for (GraphPoint graphPoint : graph.getAllVertices()) {
            String name = graphPoint.getName().name();
            Node nodejs = graphStreamGraph.addNode(name);
            nodejs.setAttribute("ui.label", name + "-" + graphPoint.getClass().getSimpleName());
            nodejs.setAttribute("ui.style", "text-size: 30px; text-color: blue; text-background-mode:none; text-alignment:under; shape:box;");
        }

        Set<String> edges = new HashSet<>();

        for (GraphPoint graphPoint : graph.getAllVertices()) {
            String source = graphPoint.getName().name();
            for (var edge : graph.getEdgesOfVertex(graphPoint)) {
                String target = edge.destination().getName().name();

                if (!edges.contains(target)) {
                    String name = source + " => " + target;
                    Edge msedge = graphStreamGraph.addEdge(name, source, target, false);


                    if (graphPoint instanceof Switch) {
                        String sideName = ((Switch) graphPoint).getSwitchSideFromPoint(edge.destination()).name();
                        msedge.setAttribute("ui.label", source + "-" + sideName);

                    }
                }
            }
            edges.add(source);
        }

        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph.setAttribute("ui.quality");
        graphStreamGraph.setAttribute("ui.antialias");

        graphStreamGraph.display();

    }

    public static void showRoutingGraph(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph) {
        org.graphstream.graph.Graph graphStreamGraph = new org.graphstream.graph.implementations.SingleGraph("Routing Graph");

        for (DirectedNode node : graph.vertexSet()) {
            Node nodejs = graphStreamGraph.addNode(node.getNodeName());
            nodejs.setAttribute("ui.label", node.getNodeName());
            nodejs.setAttribute("ui.style", "text-size: 30px; text-color: red; text-background-mode:none; text-alignment:under;");

        }

        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            DirectedNode source = graph.getEdgeSource(edge);
            DirectedNode target = graph.getEdgeTarget(edge);
            Edge msedge = graphStreamGraph.addEdge(source.getNodeName() + " => " + target.getNodeName(), source.getNodeName(), target.getNodeName(), true);
            msedge.setAttribute("ui.style", "arrow-size: 8px;");
            msedge.setAttribute("ui.arrow", "true");
            msedge.setAttribute("ui.label", graph.getEdgeWeight(edge));

        }

        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph.setAttribute("ui.quality");
        graphStreamGraph.setAttribute("ui.antialias");

        graphStreamGraph.display();

//        Viewer viewer = graphStreamGraph.display();
//        viewer.enableAutoLayout();

    }

}
