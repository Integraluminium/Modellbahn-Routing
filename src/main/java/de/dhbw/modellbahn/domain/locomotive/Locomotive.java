package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocRessources;

public class Locomotive {
    private LocRessources resources;
    private final LocId id;
    private final String name;


    public Locomotive(LocId locId, String name) {
        this.id = locId;
        this.name = name;
    }

    public LocId getLocId() {
        return id;
    }

    private void setFuel(FuelType fuelType, FuelValue fuelValue){

    }

    public void getFuel(FuelType fuelType) {

    }
    public void getAllFuels() {

    }
}
