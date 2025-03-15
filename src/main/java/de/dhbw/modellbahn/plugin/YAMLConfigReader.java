package de.dhbw.modellbahn.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.dhbw.modellbahn.adapter.locomotive.reading.ConfigLocomotive;
import de.dhbw.modellbahn.adapter.moba.config.ApiConfig;
import de.dhbw.modellbahn.adapter.track.generation.*;
import de.dhbw.modellbahn.domain.ConfigReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class YAMLConfigReader implements ConfigReader {
    private final String projectDir = System.getProperty("user.dir");
    private final String configPath = projectDir + "/src/main/resources/config/";
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    private String readFile(String path) {
        try {
            return Files.readString(Path.of(configPath + path));
        } catch (IOException e) {
            throw new RuntimeException("Could not find YAML config file at" + path + "\n" + e);
        }
    }

    private <T> T mapFileToObject(String file, Class<T> objectClass) {
        try {
            return mapper.readValue(file, objectClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not parse YAML file for " + objectClass + "\n" + e);
        }
    }

    private <T> T readObjectFromPath(String path, Class<T> objectClass) {
        String file = this.readFile(path);
        return mapFileToObject(file, objectClass);
    }

    @Override
    public List<Integer> getValidLocIds() {
        return List.of(this.readObjectFromPath("locs/loc_ids.yaml", Integer[].class));
    }

    @Override
    public List<ConfigLocomotive> getLocomotives() {
        return List.of(this.readObjectFromPath("locs/locs.yaml", ConfigLocomotive[].class));
    }

    @Override
    public ApiConfig getApiConfig() {
        return null;
    }

    @Override
    public List<ConfigConnection> getConnections() {
        return List.of(this.readObjectFromPath("track/connections.yaml", ConfigConnection[].class));
    }

    @Override
    public List<ConfigCrossSwitch> getCrossSwitches() {
        return List.of(this.readObjectFromPath("track/cross_switches.yaml", ConfigCrossSwitch[].class));
    }

    @Override
    public List<ConfigNormalSwitch> getNormalSwitches() {
        return List.of(this.readObjectFromPath("track/normal_switches.yaml", ConfigNormalSwitch[].class));
    }

    @Override
    public List<ConfigThreeWaySwitch> getThreeWaySwitches() {
        return List.of(this.readObjectFromPath("track/three_way_switches.yaml", ConfigThreeWaySwitch[].class));
    }

    @Override
    public List<ConfigTrackContact> getTrackContacts() {
        return List.of(this.readObjectFromPath("track/track_contacts.yaml", ConfigTrackContact[].class));
    }

    @Override
    public List<ConfigVirtualPoint> getVirtualPoints() {
        return List.of(this.readObjectFromPath("track/virtual_points.yaml", ConfigVirtualPoint[].class));
    }

    @Override
    public List<ConfigSignal> getSignals() {
        return List.of(this.readObjectFromPath("track/signals.yaml", ConfigSignal[].class));
    }
}