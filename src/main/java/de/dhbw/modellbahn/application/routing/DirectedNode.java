package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointSide;

public class DirectedNode {
    private final GraphPoint undirectedGraphPoint;
    private final PointSide side;

    public DirectedNode(final GraphPoint undirectedGraphPoint, final PointSide side) {
        this.undirectedGraphPoint = undirectedGraphPoint;
        this.side = side;
    }

    public String getNodeName() {
        return undirectedGraphPoint.getName() + side;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + getNodeName() + '}';
    }
}
