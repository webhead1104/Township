package me.webhead1104.towncraft.features.factories;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@DependsOn({ItemType.class})
public class FactoryType implements DataLoader.KeyBasedDataLoader<FactoryType.Factory> {
    private final Map<Key, Factory> values = new HashMap<>();

    @Override
    public void load() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            List<Factory> factories = new ArrayList<>(getListFromMultipleFiles("/data/factories", Factory.class));
            for (Factory factory : factories) {
                values.put(factory.key(), factory);
            }
            Towncraft.logger.info("Loaded {} factories in {} ms!", values.size(), stopwatch.stop().elapsed().toMillis());

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

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Factory extends Keyed {
        @Required
        @Setting("key")
        private Key key;
        @Setting("name")
        private String name;
        @Required
        @Setting("recipes")
        private List<RecipeType.Recipe> recipes;
        @Setting("menu_title")
        private Component menuTitle;
        @Required
        @Setting("building_key")
        private Key buildingKey;

        @PostProcess
        private void postProcess() {
            if (name == null) {
                name = Utils.thing2(key.value());
            }
            if (menuTitle == null) {
                menuTitle = Msg.format("<gold>%s", name);
            }
        }
    }
}
