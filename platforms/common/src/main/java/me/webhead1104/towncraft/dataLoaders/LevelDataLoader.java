package me.webhead1104.towncraft.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;

public class LevelDataLoader implements DataLoader.IntegerBasedDataLoader<LevelDataLoader.Level> {
    public static final int MAX_LEVELS_ADDED = 10;
    private final List<Level> values = new ArrayList<>();

    public Level get(int i) {
        try {
            return values.get(i);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public List<Level> list() {
        return values;
    }

    @Override
    public void load() {
        try {
            List<Level> list = getListFromFile("/data/levels.json", Level.class);
            values.addAll(list);
            Towncraft.getLogger().info("Loaded {} levels!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading levels! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Level {
        @Required
        @Setting("xp_needed")
        private int xpNeeded;
        @Required
        @Setting("coins_given")
        private int coinsGiven;
        @Required
        @Setting("cash_given")
        private int cashGiven;
    }
}
