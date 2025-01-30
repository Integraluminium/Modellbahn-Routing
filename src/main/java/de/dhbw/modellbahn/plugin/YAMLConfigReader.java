package de.dhbw.modellbahn.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.dhbw.modellbahn.adapter.api.ApiConfig;
import de.dhbw.modellbahn.adapter.graph_mapping.ConfigConnection;
import de.dhbw.modellbahn.adapter.graph_mapping.ConfigCrossSwitch;
import de.dhbw.modellbahn.adapter.graph_mapping.ConfigNormalSwitch;
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

    private <T> T mapFileToObject(String file, Class<T> tClass) {
        try {
            return mapper.readValue(file, tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not parse YAML file for " + tClass + "\n" + e);
        }
    }

    private <T> T readObjectFromPath(String path, Class<T> tClass) {
        String file = this.readFile(path);
        return mapFileToObject(file, tClass);
    }

    @Override
    public List<Integer> getValidLocIds() {
        return List.of(this.readObjectFromPath("locs/loc_ids.yaml", Integer[].class));
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
        return List.of(this.readObjectFromPath("track/cross_switches.yaml", ConfigNormalSwitch[].class));
    }
}