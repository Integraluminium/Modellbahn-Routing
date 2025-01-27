package de.dhbw.modellbahn.domain.graph2;

public abstract class DirectedNode {
    private final String name;
    private final NODE_DIRECTION direction;

    public DirectedNode(final String name, final NODE_DIRECTION direction) {
        this.name = name;
        this.direction = direction;
    }

    public String getNodeName() {
        return name + direction;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + name + direction + '}';
    }
}
