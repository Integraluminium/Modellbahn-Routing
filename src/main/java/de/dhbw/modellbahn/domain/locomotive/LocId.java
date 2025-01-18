package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.ConfigReader;

import java.util.List;

public record LocId(int id, ConfigReader configReader) {
    public LocId {
        List<Integer> validIds = configReader.getValidLocIds();
        if (!validIds.contains(id)) {
            throw new IllegalArgumentException("Id " + id + "is not a valid loc id.");
        }
    }
}
