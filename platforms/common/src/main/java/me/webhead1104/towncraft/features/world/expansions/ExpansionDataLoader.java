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
            Towncraft.getLogger().info("Loaded {} expansions!", values.size());
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
        @Setting("xp_needed")
        private int xpNeeded;
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
