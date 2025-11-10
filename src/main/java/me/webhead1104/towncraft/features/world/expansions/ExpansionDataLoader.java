package me.webhead1104.towncraft.features.world.expansions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ExpansionDataLoader implements DataLoader.IntegerBasedDataLoader<ExpansionDataLoader.Expansion> {
    private final List<Expansion> values = new ArrayList<>();

    public Expansion get(int i) {
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
    public List<Expansion> list() {
        return values;
    }

    @Override
    public void load() {
        try {
            List<Expansion> list = getListFromFile("/data/expansions.json", Expansion.class);
            values.addAll(list);
            Towncraft.logger.info("Loaded {} expansions!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading expansions! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @NoArgsConstructor
    @ConfigSerializable
    public static class Expansion {
        @Required
        @Setting("population_needed")
        private int populationNeeded;
        @Required
        @Setting("coins_needed")
        private int coinsNeeded;
        @Required
        @Setting("time")
        private Duration time;
        @Required
        @Setting("xp")
        private int xp;
        @Nullable
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
