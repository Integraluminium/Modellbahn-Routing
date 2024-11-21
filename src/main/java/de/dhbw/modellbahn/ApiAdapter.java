package de.dhbw.modellbahn;

public interface ApiAdapter {
    void setLocSpeed(LocId locId, Speed speed);
    void setLocFunction(LocId locId, LocFunction locFunction);
    void setLocDirection(LocId locId, LocDirection locDirection);
    LocDirection getLocDirection(LocId locId);
    void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue);
    Fuel getFuel(LocId locId, FuelType fuelType);
    List<Fuel> getAllFuels(LocId locId);
    void emergencyStopLoc();
    
    void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentStatus trackComponentStatus);
    TrackComponentStatus getTrackComponentStatus(TrackComponentId trackComponentId);

    void systemStop();
    void systemGo();
}
