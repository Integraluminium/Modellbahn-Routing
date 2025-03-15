package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.ConfigReader;

import java.util.List;

public final class LocId {
    private final int id;

    public LocId(int id, ConfigReader configReader) {
        List<Integer> validIds = configReader.getValidLocIds();
        if (!validIds.contains(id)) {
            throw new IllegalArgumentException("Id " + id + " is not a valid loc id.");
        }
        this.id = id;
    }

    public boolean hasId(int id) {
        return this.id == id;
    }

    public int id() {
        return id;
    }

    @Override
    public String toString() {
        return "LocId[" +
                "id=" + id + ']';
    }

}
