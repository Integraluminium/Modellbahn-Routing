package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocRessources;
import de.dhbw.modellbahn.domain.track_components.TrackComponentId;
import de.dhbw.modellbahn.domain.track_components.TrackComponentState;

public interface ApiAdapter {
    void setLocSpeed(LocId locId, Speed speed);

    void setLocFunction(LocId locId, LocFunction locFunction, boolean enabled);

    void setLocDirection(LocId locId, LocDirection locDirection);

    LocDirection getLocDirection(LocId locId);

    void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue);

    FuelValue getFuel(LocId locId, FuelType fuelType);

    LocRessources getAllFuels(LocId locId);

    void emergencyStopLoc(LocId locId);

    void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentState trackComponentStatus);

    TrackComponentState getTrackComponentStatus(TrackComponentId trackComponentId);

    void systemStop();

    void systemGo();
}
