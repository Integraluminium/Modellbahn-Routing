package de.dhbw.modellbahn.domain.graphmodellation;

import de.dhbw.modellbahn.domain.trackcomponents.SwitchComponent;

public class ThreeWaySwitch extends GraphPoint{
    GraphPoint straight;
    GraphPoint left;
    GraphPoint right;
    GraphPoint trunk;
    SwitchComponent firstSwitch;
    SwitchComponent secondSwitch;

}
