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
