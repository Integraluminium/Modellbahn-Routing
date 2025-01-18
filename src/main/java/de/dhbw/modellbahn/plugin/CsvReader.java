package de.dhbw.modellbahn.plugin;

import de.dhbw.modellbahn.adapter.api.ApiConfig;
import de.dhbw.modellbahn.domain.ConfigReader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CsvReader implements ConfigReader {
    private final String projectDir = System.getProperty("user.dir");
    private final String configPath = projectDir + "/src/main/java/de/dhbw/modellbahn/config/";
    private List<Integer> locIds = null;

    private List<Integer> readIntegerList(String filepath) throws IOException {
        List<Integer> integers;
        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            List<List<String>> records = lines.map(line -> Arrays.asList(line.split(","))).toList();
            integers = records.stream().flatMap(List::stream).map(Integer::valueOf).toList();
        }
        return integers;
    }

    @Override
    public List<Integer> getValidLocIds() {
        if (locIds == null) {
            try {
                locIds = readIntegerList(configPath + "LocIds.csv");
            } catch (IOException e) {
                throw new RuntimeException("Could not find csv file containing the loc ids.\n" + e);
            }
        }
        return Collections.unmodifiableList(locIds);
    }

    @Override
    public ApiConfig getApiConfig() {
        return null;
    }
}
