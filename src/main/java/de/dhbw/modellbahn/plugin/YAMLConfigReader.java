package de.dhbw.modellbahn.plugin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.dhbw.modellbahn.adapter.api.ApiConfig;
import de.dhbw.modellbahn.adapter.graph_mapping.Connection;
import de.dhbw.modellbahn.adapter.graph_mapping.ConnectionsConfig;
import de.dhbw.modellbahn.domain.ConfigReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class YAMLConfigReader implements ConfigReader {
    private final String projectDir = System.getProperty("user.dir");
    private final String configPath = projectDir + "/src/main/resources/config/";
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private List<Integer> locIds = null;
    private List<Connection> connections = null;

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

    @Override
    public List<Integer> getValidLocIds() {
        if (locIds == null) {
            String file = this.readFile("locs/loc_ids.yaml");
            locIds = mapFileToObject(file, LocIdsConfig.class).locIds();
        }
        return Collections.unmodifiableList(locIds);
    }

    @Override
    public ApiConfig getApiConfig() {
        return null;
    }

    @Override
    public List<Connection> getConnections() {
        if (this.connections == null) {
            String file = this.readFile("track/connections.yaml");
            connections = mapFileToObject(file, ConnectionsConfig.class).connections();
        }
        return Collections.unmodifiableList(this.connections);
    }
}