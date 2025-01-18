package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.CsvReader;

import java.io.IOException;
import java.util.List;

public record LocId(int id) {
    public LocId {
        try {
            String projectDir = System.getProperty("user.dir");
            String csvPath = "/src/main/java/de/dhbw/modellbahn/domain/locomotive/LocIds.csv";
            List<Integer> validIds = CsvReader.readIntegerList(projectDir + csvPath);
            if(!validIds.contains(id)){
                throw new IllegalArgumentException("Id " + id + "is not a valid loc id.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not find csv file containing the loc ids.\n" + e);
        }
    }
}
