package de.dhbw.modellbahn.domain.graphmodellation;

import de.dhbw.modellbahn.domain.trackcomponents.SwitchComponent;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentState;

public class NormalSwitch extends GraphPoint{
    GraphPoint trunk;
    GraphPoint strait;
    GraphPoint diverging;
    SwitchComponent switchComponent;

    Connection straight;
    Connection right;

    public void switchToConnectPoints(GraphPoint point1, GraphPoint point2){
        if(straight.connects(point1, point2)){
            switchComponent.setState(new TrackComponentState(true));
        }else if (right.connects(point1, point2)){
            switchComponent.setState(new TrackComponentState(false));
        }else{
            throw new IllegalArgumentException("Points cannot be connected by this switch.");
        }
    }
}
