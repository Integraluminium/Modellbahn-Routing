package de.dhbw.modellbahn.application.routing;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.graph.PointSide;

public class DirectedNode {
    private final GraphPoint point;
    private final PointSide side;

    /**
     * Creates a new directed node.
     *
     * @param undirectedGraphPoint The GraphPoint e.g. a track component or a virtual point
     * @param side                 Side of entry of the GraphPoint, used to determine direction
     */
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
        return point.getName().name() + "-" + side;
    }

    @Override
    public int hashCode() {
        int result = getPoint().hashCode();
        result = 31 * result + getSide().hashCode();
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        DirectedNode that = (DirectedNode) o;
        return getPoint().equals(that.getPoint()) && getSide() == that.getSide();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + getNodeName() + '}';
    }
}
