package de.dhbw.modellbahn.adapter.routing.directional_graph;

public enum NodeDirection {
    W,
    S;

    public static NodeDirection reverse(NodeDirection direction) {
        return direction == S ? W : S;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
