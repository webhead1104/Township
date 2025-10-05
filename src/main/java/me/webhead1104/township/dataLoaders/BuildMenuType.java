package me.webhead1104.township.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BuildMenuType implements DataLoader {
    public static final Map<Key, BuildMenu> values = new LinkedHashMap<>();

    public static Collection<BuildMenu> values() {
        return values.values();
    }

    public static BuildMenu get(Key key) {
        return values.get(key);
    }

    @Override
    public void load() {
        try {
            List<BuildMenu> list = getListFromFile("/data/buildMenuTypes.json", BuildMenu.class);
            for (BuildMenu buildMenu : list) {
                values.put(buildMenu.getKey(), buildMenu);
            }
            Township.logger.info("Loaded {} build menus!", values.size());
        } catch (Exception e) {
            throw new RuntimeException(
                    "An error occurred whilst loading build menus! Please report the following stacktrace to Webhead1104:",
                    e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static final class BuildMenu {
        @Setting("key")
        private Key key;
        @Setting("menu_title")
        private Component menuTitle;
        @Setting("buildings")
        private List<Key> buildings;
    }
}
