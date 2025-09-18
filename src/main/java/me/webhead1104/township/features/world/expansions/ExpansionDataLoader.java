package me.webhead1104.township.features.world.expansions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.DataLoader;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class ExpansionDataLoader implements DataLoader {
    @Getter
    private static final List<Expansion> expansions = new ArrayList<>();

    public static Expansion get(int i) {
        try {
            if (i == 1) {
                return expansions.getFirst();
            }
            return expansions.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void load() {
        try {
            long start = System.currentTimeMillis();
            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER.source(() -> new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/expansions.json"))))).build().load();
            var list = node.getList(Expansion.class);
            if (list == null || list.isEmpty()) {
                throw new RuntimeException("No expansions found!");
            }
            expansions.addAll(list);
            Township.logger.info("Loaded {} expansions in {} ms!", expansions.size(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading expansions! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @NoArgsConstructor
    @ConfigSerializable
    public static class Expansion {
        @Setting("population_needed")
        private int populationNeeded;
        @Setting("coins_needed")
        private int coinsNeeded;
        @Setting("time")
        private Duration time;
        @Setting("xp")
        private int xp;
        @Setting("tools_needed")
        private ToolsNeeded toolsNeeded;

        @ConfigSerializable
        public static class ToolsNeeded {
            @Setting("axe")
            private int axe;
            @Setting("shovel")
            private int shovel;
            @Setting("saw")
            private int saw;
        }
    }
}
