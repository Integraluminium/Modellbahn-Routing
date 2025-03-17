package de.dhbw.modellbahn.application;

import de.dhbw.modellbahn.domain.locomotive.LocId;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;

import java.util.Set;

public interface LocomotiveRepository {
    boolean existsLocomotive(LocId locId);

    Locomotive getLocomotive(LocId locId);

    Set<LocId> getAvailableLocIds();

    void addLocomotive(Locomotive loc);
}
