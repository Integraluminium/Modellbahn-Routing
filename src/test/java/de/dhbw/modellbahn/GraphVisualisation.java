package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.physical.railway.communication.calls.MockedTrackComponentCalls;
import de.dhbw.modellbahn.adapter.track.generation.GraphGenerator;
import de.dhbw.modellbahn.application.ConfigReader;
import de.dhbw.modellbahn.application.routing.directed.graph.DirectedNode;
import de.dhbw.modellbahn.domain.graph.Graph;
import de.dhbw.modellbahn.domain.graph.nodes.nonswitches.GraphPoint;
import de.dhbw.modellbahn.domain.graph.nodes.switches.Switch;
import de.dhbw.modellbahn.domain.physical.railway.communication.TrackComponentCalls;
import de.dhbw.modellbahn.plugin.DomainGraphFactory;
import de.dhbw.modellbahn.plugin.YAMLConfigReader;
import de.dhbw.modellbahn.plugin.routing.jgrapht.mapper.GraphToRoutingGraphMapper;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is used to visualize the generated graph.
 * <p>
 * IMPORTANT NOTE:
 * Moving the Graph: arrow keys
 * Zooming: Page up/Page down
 * Reset view: shift-R
 */
@SuppressWarnings("UtilityClassCanBeEnum")
public class GraphVisualisation {
    public static void main(String[] args) {
        boolean mocked = true;

        System.out.println(java.awt.GraphicsEnvironment.isHeadless());
        Graph graph;
        if (mocked) {
            graph = DomainGraphFactory.createTestGraph();
        } else {
            TrackComponentCalls calls = new MockedTrackComponentCalls();
            ConfigReader configReader = new YAMLConfigReader();
            GraphGenerator generator = new GraphGenerator(configReader, calls);
            graph = generator.generateGraph();
        }


        // Arrange
        GraphToRoutingGraphMapper mapper = new GraphToRoutingGraphMapper();
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
            nodejs.setAttribute("ui.style", "text-size: 15px; text-color: blue; text-background-mode:none; text-alignment:under; shape:box;");
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
                        msedge.setAttribute("ui.label", source + "-" + sideName + "#" + edge.distance().value());
                    } else {
                        msedge.setAttribute("ui.label", "#" + edge.distance().value());
                    }
                    msedge.setAttribute("ui.style", "text-size: 10px; text-color: black;");
                }
            }
            edges.add(source);
        }

        String styleSheet =
                """
                        node {
                            size: 3px;
                            fill-color: #777;
                            z-index: 0;
                        }
                        
                        edge {
                            shape: line;
                            fill-color: #222;
                            arrow-size: 3px, 2px;
                            text-size: 30px;
                        }
                        
                        """;

        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph.setAttribute("ui.quality");
        graphStreamGraph.setAttribute("ui.antialias");
        graphStreamGraph.setAttribute("ui.stylesheet", styleSheet);

        Viewer viewer = graphStreamGraph.display();
        viewer.enableAutoLayout();
        ViewPanel view = (ViewPanel) viewer.getDefaultView();
        view.resizeFrame(1200, 800);


    }

    public static void showRoutingGraph(org.jgrapht.Graph<DirectedNode, DefaultWeightedEdge> graph) {
        org.graphstream.graph.Graph graphStreamGraph = new org.graphstream.graph.implementations.SingleGraph("Routing Graph");

        for (DirectedNode node : graph.vertexSet()) {
            Node nodejs = graphStreamGraph.addNode(node.getNodeName());
            nodejs.setAttribute("ui.label", node.getNodeName());
            nodejs.setAttribute("ui.style", "text-size: 10px; text-color: red; text-background-mode:none; text-alignment:under;");

        }

        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            DirectedNode source = graph.getEdgeSource(edge);
            DirectedNode target = graph.getEdgeTarget(edge);
            Edge msedge = graphStreamGraph.addEdge(source.getNodeName() + " => " + target.getNodeName(), source.getNodeName(), target.getNodeName(), true);
            msedge.setAttribute("ui.style", "arrow-size: 4px;");
            msedge.setAttribute("ui.arrow", "true");
            msedge.setAttribute("ui.label", graph.getEdgeWeight(edge));
            msedge.setAttribute("ui.style", "text-size: 10px; text-color: black;");

        }

        String styleSheet =
                """
                        node {
                            size: 3px;
                            fill-color: #777;
                            z-index: 0;
                        }
                        
                        edge {
                            shape: line;
                            fill-color: #222;
                            arrow-size: 3px, 2px;
                            text-size: 30px;
                        }
                        
                        """;

        System.setProperty("org.graphstream.ui", "swing");
        graphStreamGraph.setAttribute("ui.quality");
        graphStreamGraph.setAttribute("ui.antialias");
        graphStreamGraph.setAttribute("ui.stylesheet", styleSheet);

        Viewer viewer = graphStreamGraph.display();
        viewer.enableAutoLayout();
        ViewPanel view = (ViewPanel) viewer.getDefaultView();
        view.resizeFrame(1200, 850);

    }

}
