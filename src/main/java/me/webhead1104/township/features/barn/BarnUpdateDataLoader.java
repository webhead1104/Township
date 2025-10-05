package me.webhead1104.township.features.barn;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.BarnUpgrade;
import me.webhead1104.township.dataLoaders.DataLoader;

import java.util.ArrayList;
import java.util.List;

public class BarnUpdateDataLoader implements DataLoader {
    @Getter
    private static final List<BarnUpgrade> values = new ArrayList<>();

    public static BarnUpgrade get(int i) {
        try {
            return values.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
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
