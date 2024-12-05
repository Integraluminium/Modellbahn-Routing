package de.dhbw.modellbahn.adapter.api;

import de.dhbw.modellbahn.adapter.api.dto.*;
import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.ressources.LocRessources;

public class LocAdapterImpl implements LocAdapter {
    private final ApiAdapterImpl apiAdapter;


    public LocAdapterImpl(ApiAdapterImpl apiAdapter) {
        this.apiAdapter = apiAdapter;
    }

    @Override
    public void setLocSpeed(LocId locId, Speed speed) {
        LocomotiveSpeedCommand command = new LocomotiveSpeedCommand(
                apiAdapter.getSenderHash(),
                false,
                locId.id(),
                speed.value()
        );
        apiAdapter.sendRequest("/loc/speed", command);

    }

    @Override
    public void setLocFunction(LocId locId, LocFunction locFunction, boolean enabled) {
        LocomotiveFunctionCommand command = new LocomotiveFunctionCommand(
                apiAdapter.getSenderHash(),
                false,
                locId.id(),
                locFunction.getFunctionID(),
                enabled ? 1 : 0,
                null
        );
        apiAdapter.sendRequest("/loc/function", command);
    }

    @Override
    public void setLocDirection(LocId locId, LocDirection locDirection) {
        LocomotiveDirectionCommand command = new LocomotiveDirectionCommand(
                apiAdapter.getSenderHash(),
                false,
                locId.id(),
                locDirection.getDirection()
        );
        apiAdapter.sendRequest("/loc/direction", command);
    }

    @Override
    public LocDirection getLocDirection(LocId locId) {
        setLocDirection(locId, LocDirection.KEEP);
        return null; // TODO implement response
    }

    @Override
    public void setFuel(LocId locId, FuelType fuelType, FuelValue fuelValue) {
        int number = 345;   // TODO

        WriteConfigCommand command = new WriteConfigCommand(
                apiAdapter.getSenderHash(),
                false,
                locId.id(),
                fuelType.getType(),
                number,
                (int) fuelValue.value(),
                new WriteConfigControlByte(
                        false,
                        false,
                        DCCProgramming.DIRECT_PROGRAMMING
                ),
                new WriteConfigResultByte()
        );
        apiAdapter.sendRequest("/loc/write_config", command);
    }

    @Override
    public FuelValue getFuel(LocId locId, FuelType fuelType) {
        return null;     // TODO
    }

    @Override
    public LocRessources getAllFuels(LocId locId) {
        return null;    // TODO
    }

    @Override
    public void emergencyStopLoc(LocId locId) {
        LocomotiveEmergencyStopCommand command = new LocomotiveEmergencyStopCommand(
                apiAdapter.getSenderHash(),
                false,
                locId.id()
        );
        apiAdapter.sendRequest("/system/locomotive_emergency_stop", command);
    }

}
