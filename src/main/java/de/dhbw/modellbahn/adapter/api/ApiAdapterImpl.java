package de.dhbw.modellbahn.adapter.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dhbw.modellbahn.adapter.api.dto.*;
import de.dhbw.modellbahn.domain.locomotive.LocDirection;
import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Speed;
import de.dhbw.modellbahn.domain.locomotive.functions.LocFunction;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelType;
import de.dhbw.modellbahn.domain.locomotive.ressources.FuelValue;
import de.dhbw.modellbahn.domain.locomotive.ressources.LocRessources;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentId;
import de.dhbw.modellbahn.domain.trackcomponents.TrackComponentState;

import java.net.http.HttpClient;

public class ApiAdapterImpl implements ApiAdapter {
    private final String url;
    private final int senderHash;


    public ApiAdapterImpl(int senderHash, String url) {
        this.senderHash = senderHash;
        this.url = url != null ? url : "http://127.0.0.1:8002";
        //  this.gson = new Gson();
    }

    private <T> void sendRequest(String url, T bodyObject) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            ObjectMapper objectMapper = new ObjectMapper();

            // Serialize the bodyObject to JSON
            String jsonBody = objectMapper.writeValueAsString(bodyObject);
            System.out.printf(jsonBody);

//
//            // Build the HttpRequest with the JSON body
//            HttpRequest request = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .header("Content-Type", "application/json") // Set the content type to application/json
//                    .timeout(Duration.ofSeconds(10)) // Set a timeout for the request
//                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody)) // Set the body
//                    .build();
//
//            // Send the request and get the response
//            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
//
//            // Check the response status code
//            if (response.statusCode() != 200) {
//                System.err.println("Request failed with status code: " + response.statusCode());
//            }
//        } catch (HttpTimeoutException e) {
//            System.err.println("Request timed out: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void setLocSpeed(LocId locId, Speed speed) {
        LocomotiveSpeedCommand command = new LocomotiveSpeedCommand(
                senderHash,
                false,
                locId.id(),
                speed.value()
        );
        sendRequest(url + "/loc/speed", command);

    }

    @Override
    public void setLocFunction(LocId locId, LocFunction locFunction, boolean enabled) {
        LocomotiveFunctionCommand command = new LocomotiveFunctionCommand(
                senderHash,
                false,
                locId.id(),
                locFunction.getFunctionID(),
                enabled ? 1 : 0,
                null
        );
        sendRequest(url + "/loc/function", command);
    }

    @Override
    public void setLocDirection(LocId locId, LocDirection locDirection) {
        LocomotiveDirectionCommand command = new LocomotiveDirectionCommand(
                senderHash,
                false,
                locId.id(),
                locDirection.getDirection()
        );
        sendRequest(url + "/loc/direction", command);
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
                senderHash,
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
        sendRequest(url + "/loc/write_config", command);
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
                senderHash,
                false,
                locId.id()
        );
        sendRequest(url + "/system/locomotive_emergency_stop", command);
    }

    @Override
    public void setTrackComponentStatus(TrackComponentId trackComponentId, TrackComponentState trackComponentStatus) {
        sendRequest(url + "/loc/switch_accessory", new SwitchingAccessoriesCommand(
                senderHash,
                false,
                trackComponentId.id(),
                trackComponentStatus.state() ? 0 : 1,
                1,
                null
        ));
    }

    @Override
    public TrackComponentState getTrackComponentStatus(TrackComponentId trackComponentId) {
        sendRequest(url + "/loc/switch_accessory", new SwitchingAccessoriesCommand(
                senderHash,
                false,
                trackComponentId.id(),
                0,
                0,
                null
        ));

        return null;    // TODO
    }

    @Override
    public void systemStop() {
        sendRequest(url + "system/stop", new SystemStopCommand(
                senderHash,
                true,
                0
        ));
    }

    @Override
    public void systemGo() {
        sendRequest(url + "system/go", new SystemGoCommand(
                senderHash,
                true,
                0
        ));

    }
}
