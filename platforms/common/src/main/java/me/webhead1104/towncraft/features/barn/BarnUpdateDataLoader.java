package me.webhead1104.towncraft.features.barn;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.BarnUpgrade;
import me.webhead1104.towncraft.dataLoaders.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class BarnUpdateDataLoader implements DataLoader.IntegerBasedDataLoader<BarnUpgrade> {
    private final List<BarnUpgrade> values = new ArrayList<>();

    @Override
    public BarnUpgrade get(int key) {
        try {
            return values.get(key);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public List<BarnUpgrade> list() {
        return values;
    }

    @Override
    public void load() {
        try {
            int storage = 50;

            for (int i = 1; i <= 100; i++) {
                int increment;

                if (i <= 3) {
                    increment = 20;
                } else if (i <= 39) {
                    increment = 25;
                } else if (i <= 59) {
                    increment = 50;
                } else {
                    increment = 75;
                }

                storage += increment;
                values.add(new BarnUpgrade(i, i, storage));
            }

            Towncraft.getLogger().info("Loaded {} barn upgrades!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading barn upgrades! Please report the following stacktrace to Webhead1104:", e);
        }
    }
}
