package de.dhbw.modellbahn.adapter.moba.communication.calls;

import de.dhbw.modellbahn.application.port.moba.communication.LocCalls;
import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.resources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.resources.LocResources;

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
