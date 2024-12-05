package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.ressources.LocRessources;

public interface LocAdapter {
    void setLocSpeed(LocId locId, Speed speed);

    void setLocFunction(LocId locId, LocFunction locFunction, boolean enabled);

    void setLocDirection(LocId locId, LocDirection locDirection);

    LocDirection getLocDirection(LocId locId);

    void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue);

    FuelValue getFuel(LocId locId, FuelType fuelType);

    LocRessources getAllFuels(LocId locId);

    void emergencyStopLoc(LocId locId);
}
