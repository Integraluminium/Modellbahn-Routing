package de.dhbw.modellbahn.application.path;

import de.dhbw.modellbahn.domain.graph2.*;
import de.dhbw.modellbahn.plugin.TrackConfigParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO, Document this class
// TODO, Make this class error proof and test it

/**
 * Service for building routing graphs.
 * <p>
 * This service is responsible for creating and managing the graph structure used for routing algorithms.
 * It provides methods to build the graph from a configuration, retrieve graph node components, and create routes.
 * The graph is built using a directed weighted graph structure, and the service supports adding routes and start nodes.
 * It also includes functionality to find the shortest path between nodes using Dijkstra's algorithm.
 */
public class RoutingGraphBuildingService {

    private final Map<String, DirectedNode> graphNodeComponents = new HashMap<>();

    public RoutingGraphBuildingService() {
    }

    /**
     * Builds the graph for the routing algorithm
     *
     * @param config build information for the graph
     * @return the graph service
     */
    public GraphService buildGraph(String config) {
        TrackConfigParser parser = new TrackConfigParser(config);
        Map<String, List<String>> data = parser.getComponentsOverview();
        createNodes(data);
        List<Route> routes = createRoutes(parser.getDirectedNodesWithNeighbours());

        GraphService graphService = new GraphService();
        routes.forEach(graphService::addRoute);

        return graphService;
    }

    /**
     * Returns the graph node component
     *
     * @param nodeName  the name of the node
     * @param direction the direction of the node
     * @return the graph node component
     */
    public DirectedNode getGraphNodeComponent(String nodeName, NODE_DIRECTION direction) {
        return graphNodeComponents.get(nodeName + direction);
    }

    /**
     * Creates the routes for the routin
     *
     * @param nodesWithNeighbours the map with the nodes and their neighbours
     * @return the list of routes
     */
    private List<Route> createRoutes(final Map<String, List<String>> nodesWithNeighbours) {
        List<Route> routes = new ArrayList<>();

        for (String nodeName : nodesWithNeighbours.keySet()) {
            for (String neighbourNodeName : nodesWithNeighbours.get(nodeName)) {
                var start = graphNodeComponents.get(nodeName);
                var end = graphNodeComponents.get(neighbourNodeName);
                var weights = 1;    // TODO calculate weights

                routes.add(new Route(start, end, weights));
            }
        }
        return routes;
    }

    /**
     * Creates the nodes for the routing
     *
     * @param componentsOverviewMap the map with the components
     */
    private void createNodes(final Map<String, List<String>> componentsOverviewMap) {
        componentsOverviewMap.get("switches").forEach(s -> {
            SwitchNode switchNodeS = new SwitchNode(s, NODE_DIRECTION.S);
            SwitchNode switchNodeW = new SwitchNode(s, NODE_DIRECTION.W);
            graphNodeComponents.put(switchNodeW.getNodeName(), switchNodeS);
            graphNodeComponents.put(switchNodeS.getNodeName(), switchNodeW);
        });
        componentsOverviewMap.get("endpoints").forEach(e -> {
            EndNode endNodeS = new EndNode(e, NODE_DIRECTION.S);
            EndNode endNodeW = new EndNode(e, NODE_DIRECTION.W);
            graphNodeComponents.put(endNodeW.getNodeName(), endNodeS);
            graphNodeComponents.put(endNodeS.getNodeName(), endNodeW);
        });
    }
}
