package de.dhbw.modellbahn.plugin;

import java.util.Collections;
import java.util.List;

public class LocIdsConfig {
    private List<Integer> locIds;

    public List<Integer> getLocIds() {
        return Collections.unmodifiableList(locIds);
    }
}
