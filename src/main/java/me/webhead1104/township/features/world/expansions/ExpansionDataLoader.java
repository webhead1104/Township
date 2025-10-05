package me.webhead1104.township.features.world.expansions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.DataLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ExpansionDataLoader implements DataLoader {
    @Getter
    private static final List<Expansion> values = new ArrayList<>();

    public static Expansion get(int i) {
        try {
            if (i == 1) {
                return values.getFirst();
            }
            return values.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public void load() {
        try {
            List<Expansion> list = getListFromFile("/data/expansions.json", Expansion.class);
            values.addAll(list);
            Township.logger.info("Loaded {} expansions!", values.size());
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
