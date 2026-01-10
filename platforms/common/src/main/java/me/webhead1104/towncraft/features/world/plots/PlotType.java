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
package me.webhead1104.towncraft.features.world.plots;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.price.Price;
import me.webhead1104.towncraft.utils.Msg;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlotType implements DataLoader.KeyBasedDataLoader<PlotType.Plot> {
    private final Map<Key, Plot> values = new LinkedHashMap<>();

    @Override
    public Plot get(Key key) {
        if (!values.containsKey(key)) {
            throw new IllegalStateException("Plot type does not exist! key:" + key.asString());
        }
        return values.get(key);
    }

    @Override
    public Collection<Key> keys() {
        return values.keySet();
    }

    @Override
    public Collection<Plot> values() {
        return values.values();
    }

    @Override
    public void load() {
        try {
            List<Plot> list = getListFromFile("/data/plots.json", Plot.class);
            for (Plot plot : list) {
                plot.postProcess();
                values.put(plot.getKey(), plot);
            }
            Towncraft.getLogger().info("Loaded {} animals!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading plots! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @NoArgsConstructor
    @ConfigSerializable
    @AllArgsConstructor
    public static class Plot extends Keyed {
        @Required
        private Key key;
        @Required
        private Price price;
        @Required
        @Setting("level_needed")
        private int levelNeeded;
        @Required
        @Setting("xp_given")
        private int xpGiven;
        private Duration time;
        private transient ItemType.Item item;
        private transient TowncraftItemStack menuItem;

        @PostProcess
        private void postProcess() {
            this.item = Towncraft.getDataLoader(ItemType.class).get(this.key);
            if (key.equals(Towncraft.NONE_KEY)) {
                TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.FARMLAND);
                itemStack.setName(Msg.format("Empty Plot"));
                itemStack.setLore(Msg.format("<green>You should plant something!"));
                menuItem = itemStack;
                return;
            }
            this.menuItem = this.item.getItemStack();
        }
    }
}
