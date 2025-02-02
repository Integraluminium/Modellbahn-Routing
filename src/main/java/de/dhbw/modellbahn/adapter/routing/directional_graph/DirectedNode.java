package de.dhbw.modellbahn.adapter.routing.directional_graph;

import de.dhbw.modellbahn.domain.graph.GraphPoint;

public class DirectedNode {
    private final GraphPoint undirectedGraphPoint;
    private final NodeDirection direction;

    public DirectedNode(final GraphPoint undirectedGraphPoint, final NodeDirection direction) {
        this.undirectedGraphPoint = undirectedGraphPoint;
        this.direction = direction;
    }

    public String getNodeName() {
        return undirectedGraphPoint.getName() + direction;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + getNodeName() + '}';
    }
}
