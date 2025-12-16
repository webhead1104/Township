package me.webhead1104.towncraft.features.factories;

import com.google.common.base.Stopwatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@DependsOn({ItemType.class})
public class FactoryType implements DataLoader.KeyBasedDataLoader<FactoryType.Factory> {
    private final Map<Key, Factory> values = new LinkedHashMap<>();

    @Override
    public void load() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            List<Factory> factories = new ArrayList<>(getListFromFile("/data/factories.json", Factory.class));
            for (Factory factory : factories) {
                if (factory.getAmount() == -1) {
                    values.put(factory.getKey(), factory);
                    continue;
                }
                Key baseKey = factory.key();
                for (int i = 1; i <= factory.getAmount(); i++) {
                    Key key = Key.key(baseKey.asString() + "_" + i);
                    Factory instance = factory.withKey(key);
                    values.put(key, instance);
                }
            }
            Towncraft.getLogger().info("Loaded {} factories in {} ms!", values.size(), stopwatch.stop().elapsed().toMillis());

        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading factories! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Override
    public Factory get(Key key) {
        if (!values.containsKey(key)) {
            throw new IllegalStateException("Factory type does not exist! key:" + key.asString());
        }
        return values.get(key);
    }

    @Override
    public Collection<Key> keys() {
        return values.keySet();
    }

    @Override
    public Collection<Factory> values() {
        return values.values();
    }

    @With
    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Factory extends Keyed {
        @Required
        private Key key;
        @Required
        private int amount = -1;
        @Required
        private String name;
        @Required
        private List<RecipeType.Recipe> recipes;
        @Required
        @Setting("building_key")
        private Key buildingKey;
        private transient Component menuTitle;

        @PostProcess
        private void postProcess() {
            menuTitle = Msg.format("<gold>%s", name);
        }
    }
}
