package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.locomotive.ressources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.ressources.LocRessources;

public class Locomotive {
    private LocRessources ressources;
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

    public FuelValue getFuel(FuelType fuelType) {

    }
    public LocRessources getAllFuels() {

    }
}
