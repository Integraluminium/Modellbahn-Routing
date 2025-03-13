package de.dhbw.modellbahn.domain.locomotive;

import de.dhbw.modellbahn.domain.ConfigReader;

import java.util.List;
import java.util.Objects;

public record LocId(int id, ConfigReader configReader) {
    public LocId {
        List<Integer> validIds = configReader.getValidLocIds();
        if (!validIds.contains(id)) {
            throw new IllegalArgumentException("Id " + id + " is not a valid loc id.");
        }
    }

    public boolean equals(int id) {
        return this.id == id;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LocId locId = (LocId) o;
        return id() == locId.id();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id());
    }
}
