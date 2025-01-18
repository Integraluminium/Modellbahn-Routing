package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.graph.GraphPoint;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocResources;

public class Locomotive {
    private final LocId id;
    private LocResources resources;
    private GraphPoint currentLocation;


    public Locomotive(LocId locId, GraphPoint startLocation) {
        this.id = locId;
        this.currentLocation = startLocation;
    }

    public LocId getLocId() {
        return id;
    }

    private void setFuel(FuelType fuelType, FuelValue fuelValue){

    }

    public FuelValue getFuel(FuelType fuelType) {
        return null;
    }
    public LocResources getAllFuels() {
        return null;
    }

    public GraphPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GraphPoint currentLocation) {
        this.currentLocation = currentLocation;
    }
}
