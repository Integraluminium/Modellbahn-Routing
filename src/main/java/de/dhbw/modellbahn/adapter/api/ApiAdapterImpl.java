package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.ressources.LocRessources;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentState;

public class ApiAdapterImpl implements ApiAdapter {
    private String url;
    private String senderHash;


    public ApiAdapterImpl(String senderHash, String url) {
        this.senderHash = senderHash;
        this.url = url != null ? url : "http://127.0.0.1:8002";
      //  this.gson = new Gson();
    }



    @Override
    public void setLocSpeed(LocId locId, Speed speed) {

    }

    @Override
    public void setLocFunction(LocId locId, LocFunction locFunction) {

    }

    @Override
    public void setLocDirection(LocId locId, LocDirection locDirection) {

    }

    @Override
    public LocDirection getLocDirection(LocId locId) {
        return null;
    }

    @Override
    public void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue) {

    }

    @Override
    public FuelValue getFuel(LocId locId, FuelType fuelType) {
        return null;
    }

    @Override
    public LocRessources getAllFuels(LocId locId) {
        return null;
    }

    @Override
    public void emergencyStopLoc() {

    }

    @Override
    public void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentState trackComponentStatus) {

    }

    @Override
    public TrackComponentState getTrackComponentStatus(TrackComponentId trackComponentId) {
        return null;
    }

    @Override
    public void systemStop() {

    }

    @Override
    public void systemGo() {

    }
}
