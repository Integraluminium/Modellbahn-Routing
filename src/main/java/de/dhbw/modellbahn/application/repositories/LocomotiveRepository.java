package de.dhbw.modellbahn.application.repositories;

import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;

import java.util.Set;

public interface LocomotiveRepository {
    boolean existsLocomotive(LocId locId);

    Locomotive getLocomotive(LocId locId);

    Set<LocId> getAvailableLocIds();

    void addLocomotive(Locomotive loc);

    void updateLocomotives();
}
