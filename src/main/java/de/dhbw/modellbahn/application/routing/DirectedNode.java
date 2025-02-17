package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointSide;

public class DirectedNode {
    private final GraphPoint point;
    private final PointSide side;

    public DirectedNode(final GraphPoint undirectedGraphPoint, final PointSide side) {
        this.point = undirectedGraphPoint;
        this.side = side;
    }

    public GraphPoint getPoint() {
        return point;
    }

    public PointSide getSide() {
        return side;
    }

    public String getNodeName() {
        return point.getName().name() + side;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + getNodeName() + '}';
    }
}
