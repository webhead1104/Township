/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.features.world.build;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.apache.commons.text.WordUtils;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

@DependsOn(BuildingType.class)
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
            List<BuildMenu> list = getListFromFile("/data/buildMenus.json", BuildMenu.class);
            for (BuildMenu buildMenu : list) {
                values.put(buildMenu.key(), buildMenu);
            }
            Towncraft.getLogger().info("Loaded {} build menus!", values.size());
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
        private List<Key> realBuildings;
        private transient Component menuTitle;
        private transient List<Key> buildings;

        @PostProcess
        private void postProcess() {
            menuTitle = Msg.format(WordUtils.capitalizeFully(key.value().replaceAll("_", " ")));
            buildings = new ArrayList<>(realBuildings.stream().map(Towncraft.getDataLoader(BuildingType.class)::get).map(List::getFirst)
                    .filter(it -> !it.isNotInMenu()).map(BuildingType.Building::getKey).toList());
        }
    }
}
