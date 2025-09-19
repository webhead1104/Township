package me.webhead1104.township.features.barn;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.BarnUpgrade;
import me.webhead1104.township.dataLoaders.DataLoader;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BarnUpdateDataLoader implements DataLoader {
    @Getter
    private static final List<BarnUpgrade> upgrades = new ArrayList<>();

    public static BarnUpgrade get(int i) {
        try {
            return upgrades.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void load() {
        try {
            long start = System.currentTimeMillis();
            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER.source(() -> new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/barnUpgrades.json"))))).build().load();
            var list = node.getList(BarnUpgrade.class);
            if (list == null || list.isEmpty()) {
                throw new RuntimeException("No barn upgrades found!");
            }
            upgrades.addAll(list);
            Township.logger.info("Loaded {} barn upgrades in {} ms!", upgrades.size(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading barn upgrades! Please report the following stacktrace to Webhead1104:", e);
        }
    }
}
