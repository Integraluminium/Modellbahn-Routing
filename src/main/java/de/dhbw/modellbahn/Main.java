package de.dhbw.modellbahn;

import de.dhbw.modellbahn.adapter.api.ApiAdapter;
import de.dhbw.modellbahn.adapter.api.ApiAdapterImpl;
import de.dhbw.modellbahn.domain.locomotive.LocId;

public class Main {
    public static void main(String[] args) {

        LocId id = new LocId(42);

        ApiAdapter adapter = new ApiAdapterImpl(0, "");
        adapter.systemStop();

    }

    public static int foo() {
        return 1;
    }
}