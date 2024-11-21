package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.ressources.LocRessources;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;

public interface ApiAdapter {
    void setLocSpeed(LocId locId, Speed speed);
    void setLocFunction(LocId locId, LocFunction locFunction);
    void setLocDirection(LocId locId, LocDirection locDirection);
    LocDirection getLocDirection(LocId locId);
    void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue);
    FuelValue getFuel(LocId locId, FuelType fuelType);
    LocRessources getAllFuels(LocId locId);
    void emergencyStopLoc();

    void setTrackComponentStatus(TrackComponentId trackComponentId, SwitchComponentState trackComponentStatus);
    SwitchComponentState getTrackComponentStatus(TrackComponentId trackComponentId);

    void systemStop();
    void systemGo();
}
