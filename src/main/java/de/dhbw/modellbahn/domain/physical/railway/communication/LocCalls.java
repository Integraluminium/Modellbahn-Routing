package de.dhbw.modellbahn.domain.physical.railway.communication;

import de.dhbw.modellbahn.domain.locomotive.attributes.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.LocResources;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;

public interface LocCalls {
    void setLocSpeed(LocId locId, Speed speed);

    void setLocFunction(LocId locId, LocFunction locFunction, boolean enabled);

    void setLocDirection(LocId locId, LocDirection locDirection);

    LocDirection getLocDirection(LocId locId);

    void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue);

    FuelValue getFuel(LocId locId, FuelType fuelType);

    LocResources getAllFuels(LocId locId);

    void emergencyStopLoc(LocId locId);
}
