package me.webhead1104.township.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LevelDataLoader implements DataLoader {
    @Getter
    private static final List<Level> levels = new ArrayList<>();

    public static Level get(int i) {
        try {
            return levels.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void load() {
        try {
            long start = System.currentTimeMillis();
            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER.source(() -> new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/levels.json"))))).build().load();
            List<Level> list = node.getList(Level.class);
            if (list == null || list.isEmpty()) {
                throw new RuntimeException("No levels found!");
            }
            levels.addAll(list);
            Township.logger.info("Loaded {} levels in {} ms!", levels.size(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading levels! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Level {
        @Setting("xp_needed")
        private int xpNeeded;
        @Setting("coins_given")
        private int coinsGiven;
        @Setting("cash_given")
        private int cashGiven;
    }
}
