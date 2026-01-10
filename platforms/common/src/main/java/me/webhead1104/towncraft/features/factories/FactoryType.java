/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
