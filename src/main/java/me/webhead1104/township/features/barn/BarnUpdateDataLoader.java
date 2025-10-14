package me.webhead1104.township.features.barn;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.BarnUpgrade;
import me.webhead1104.township.dataLoaders.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class BarnUpdateDataLoader implements DataLoader.IntegerBasedDataLoader<BarnUpgrade> {
    @Getter
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
            List<BarnUpgrade> list = getListFromFile("/data/barnUpgrades.json", BarnUpgrade.class);
            values.addAll(list);
            Township.logger.info("Loaded {} barn upgrades!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading barn upgrades! Please report the following stacktrace to Webhead1104:", e);
        }
    }
}
