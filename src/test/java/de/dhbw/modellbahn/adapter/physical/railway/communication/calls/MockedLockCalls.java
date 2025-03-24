package de.dhbw.modellbahn.adapter.physical.railway.communication.calls;

import de.dhbw.modellbahn.domain.locomotive.attributes.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.locomotive.attributes.Speed;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.attributes.resources.LocResources;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.physical.railway.communication.LocCalls;

public class MockedLockCalls implements LocCalls {
    @Override
    public void setLocSpeed(final LocId locId, final Speed speed) {

    }

    @Override
    public void setLocFunction(final LocId locId, final LocFunction locFunction, final boolean enabled) {

    }

    @Override
    public void setLocDirection(final LocId locId, final LocDirection locDirection) {

    }

    @Override
    public LocDirection getLocDirection(final LocId locId) {
        return null;
    }

    @Override
    public void setFuel(final LocId locId, final FuelType fuelType, final FuelValue fuelValue) {

    }

    @Override
    public FuelValue getFuel(final LocId locId, final FuelType fuelType) {
        return null;
    }

    @Override
    public LocResources getAllFuels(final LocId locId) {
        return null;
    }

    @Override
    public void emergencyStopLoc(final LocId locId) {

    }
}
