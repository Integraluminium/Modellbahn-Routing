package de.dhbw.modellbahn.domain.graph.nodes.nonswitches;

import de.dhbw.modellbahn.domain.graph.nodes.attributes.PointName;
import de.dhbw.modellbahn.domain.physical.railway.components.SignalComponent;
import de.dhbw.modellbahn.domain.physical.railway.components.SignalState;

public class Signal extends GraphPoint {
    private final SignalComponent signalComponent;

    public Signal(PointName name, SignalComponent signalComponent) {
        super(name);
        this.signalComponent = signalComponent;
    }

    public void setSignalState(SignalState state) {
        signalComponent.setState(state);
    }
}
