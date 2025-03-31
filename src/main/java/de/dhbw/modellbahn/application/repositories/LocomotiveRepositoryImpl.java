package de.dhbw.modellbahn.application.repositories;

import de.dhbw.modellbahn.adapter.locomotive.reading.LocomotiveReader;
import de.dhbw.modellbahn.application.ConfigReader;
import de.dhbw.modellbahn.domain.locomotive.Locomotive;
import de.dhbw.modellbahn.domain.locomotive.attributes.LocId;
import de.dhbw.modellbahn.domain.physical.railway.communication.LocCalls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocomotiveRepositoryImpl implements LocomotiveRepository {
    private final LocCalls locCalls;
    private final Map<LocId, Locomotive> locomotiveMap;
    private final ConfigReader configReader;

    public LocomotiveRepositoryImpl(final ConfigReader configReader, final LocCalls locCalls) {
        this.locCalls = locCalls;
        this.locomotiveMap = new HashMap<>();
        this.configReader = configReader;
        loadLocomotives(configReader);
    }

    private void loadLocomotives(ConfigReader configReader) {
        List<Locomotive> locomotiveList = new LocomotiveReader(configReader).readLocomotives(locCalls);
        for (Locomotive loc : locomotiveList) {
            locomotiveMap.put(loc.getLocId(), loc);
        }
    }

    @Override
    public boolean existsLocomotive(final LocId locId) {
        return locomotiveMap.containsKey(locId);
    }

    @Override
    public Locomotive getLocomotive(final LocId locId) {
        if (existsLocomotive(locId)) {
            return locomotiveMap.get(locId);
        } else {
            throw new IllegalArgumentException("Locomotive with id " + locId.id() + " does not exist.");
        }
    }

    @Override
    public Set<LocId> getAvailableLocIds() {
        return this.locomotiveMap.keySet();
    }

    @Override
    public void addLocomotive(final Locomotive loc) {
        locomotiveMap.put(loc.getLocId(), loc);
    }

    @Override
    public void updateLocomotives() {
        this.configReader.updateLocomotives(this.locomotiveMap);
    }
}
