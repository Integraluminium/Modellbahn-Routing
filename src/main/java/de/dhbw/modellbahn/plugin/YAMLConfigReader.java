package de.dhbw.modellbahn.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.dhbw.modellbahn.adapter.api.ApiConfig;
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

    @Override
    public List<Integer> getValidLocIds() {
        if (locIds == null) {
            try {
                String file = Files.readString(Path.of(configPath + "locs/loc_ids.yaml"));
                locIds = mapper.readValue(file, LocIdsConfig.class).getLocIds();
            } catch (IOException e) {
                throw new RuntimeException("Could not find YAML file containing the loc ids.\n" + e);
            }
        }
        return Collections.unmodifiableList(locIds);
    }

    @Override
    public ApiConfig getApiConfig() {
        return null;
    }
}
