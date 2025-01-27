package de.dhbw.modellbahn.application.path;

import de.dhbw.modellbahn.domain.graph2.*;
import de.dhbw.modellbahn.plugin.TrackConfigParser2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO, Document this class
// TODO, Make this class error proof and test it
public class RoutingGraphBuildingService {

    private final Map<String, DirectedNode> graphNodeComponents = new HashMap<>();

    public RoutingGraphBuildingService() {
    }

    public GraphService buildGraph(String config) {
        TrackConfigParser2 parser = new TrackConfigParser2(config);
        Map<String, List<String>> data = parser.getComponentsOverview();
        createNodes(data);
        List<Route> routes = createRoutes(parser.getDirectedNodesWithNeighbours());

        GraphService graphService = new GraphService();
        routes.forEach(graphService::addRoute);

        return graphService;
    }

    public DirectedNode getGraphNodeComponent(String nodeName, NODE_DIRECTION direction) {
        return graphNodeComponents.get(nodeName + direction);
    }

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
