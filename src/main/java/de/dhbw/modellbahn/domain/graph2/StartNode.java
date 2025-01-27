package de.dhbw.modellbahn.domain.graph2;

public class StartNode extends DirectedNode {
    public StartNode() {
        super(
                "Start", // Start node is always called "Start"
                NODE_DIRECTION.W //Direction is not needed for start node
        );
    }
}
