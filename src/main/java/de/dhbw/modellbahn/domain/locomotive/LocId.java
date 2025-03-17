package de.dhbw.modellbahn.domain.locomotive;

public record LocId(int id) {
    public LocId {
        if (id <= 0) {
            throw new IllegalArgumentException("Id " + id + " is not a valid loc id.");
        }
    }

    public boolean hasId(int id) {
        return this.id == id;
    }

    @Override
    public String toString() {
        return "LocId[" +
                "id=" + id + ']';
    }

}
