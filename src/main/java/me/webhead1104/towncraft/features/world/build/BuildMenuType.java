package me.webhead1104.towncraft.features.world.build;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildMenuType implements DataLoader.KeyBasedDataLoader<BuildMenuType.BuildMenu> {
    private final Map<Key, BuildMenu> values = new LinkedHashMap<>();

    public BuildMenu get(Key key) {
        if (!values.containsKey(key)) {
            throw new IllegalStateException("Build menu type does not exist! key:" + key.asString());
        }
        return values.get(key);
    }

    @Override
    public Collection<Key> keys() {
        return values.keySet();
    }

    public Collection<BuildMenu> values() {
        return values.values();
    }

    @Override
    public void load() {
        try {
            List<BuildMenu> list = getListFromFile("/data/buildMenuTypes.json", BuildMenu.class);
            for (BuildMenu buildMenu : list) {
                values.put(buildMenu.key(), buildMenu);
            }
            Towncraft.logger.info("Loaded {} build menus!", values.size());
        } catch (Exception e) {
            throw new RuntimeException(
                    "An error occurred whilst loading build menus! Please report the following stacktrace to Webhead1104:",
                    e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static final class BuildMenu extends Keyed {
        @Required
        @Setting("key")
        private Key key;
        @Required
        @Setting("buildings")
        private List<Key> buildings;
        @Required
        @Setting("menu_title")
        private Component menuTitle;
    }
}
