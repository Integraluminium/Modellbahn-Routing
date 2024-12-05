package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocResources;

public class Locomotive {
    private LocResources resources;
    private final LocId id;


    public Locomotive(LocId locId) {
        this.id = locId;
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
}
