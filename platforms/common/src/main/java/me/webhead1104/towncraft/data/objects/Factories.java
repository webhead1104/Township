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
package me.webhead1104.towncraft.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.features.factories.FactoryType;
import me.webhead1104.towncraft.features.factories.RecipeType;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigSerializable
public class Factories {
    private final Map<Key, Factory> factoryBuildings = new HashMap<>();

    public Factories() {
        for (FactoryType.Factory value : Towncraft.getDataLoader(FactoryType.class).values()) {
            factoryBuildings.put(value.key(), new Factory());
        }
    }

    public Factory getFactory(Key factoryType) {
        if (factoryBuildings.containsKey(factoryType)) {
            return factoryBuildings.get(factoryType);
        }
        Factory factory = new Factory();
        factoryBuildings.put(factoryType, factory);
        return factory;
    }

    @ConfigSerializable
    @Getter
    @Setter
    public static class Factory {
        private final Map<Integer, Key> waiting = new HashMap<>();
        private final Map<Integer, Key> completed = new HashMap<>();
        private Key workingOn;
        private Instant instant;

        public Factory() {
            for (int i = 0; i < 3; i++) {
                this.waiting.put(i, Towncraft.NONE_KEY);
                this.completed.put(i, Towncraft.NONE_KEY);
            }
            this.workingOn = Towncraft.NONE_KEY;
            this.instant = Instant.EPOCH;
        }

        public RecipeType.Recipe getWaiting(int slot) {
            return Towncraft.getDataLoader(RecipeType.class).get(waiting.get(slot));
        }

        public void setCompleted(int slot, Key item) {
            completed.put(slot, item);
        }

        public RecipeType.Recipe getCompleted(int slot) {
            return Towncraft.getDataLoader(RecipeType.class).get(completed.get(slot));
        }

        public RecipeType.Recipe getWorkingOn() {
            return Towncraft.getDataLoader(RecipeType.class).get(workingOn);
        }

        public RecipeType.Recipe removeFirstWaiting() {
            RecipeType.Recipe recipe = getWaiting(0);
            for (int i = 1; i < 3; i++) {
                waiting.put(i - 1, waiting.get(i));
            }
            waiting.put(2, Towncraft.NONE_KEY);

            return recipe;
        }

        public void addWaiting(Key key) {
            for (int i = 0; i < 3; i++) {
                if (!waiting.get(i).equals(Towncraft.NONE_KEY)) continue;
                waiting.put(i, key);
                return;
            }
        }

        public boolean hasWaiting() {
            for (Key value : waiting.values()) {
                if (!value.equals(Towncraft.NONE_KEY)) return true;
            }
            return false;
        }

        public boolean canAddWaiting() {
            for (Key value : waiting.values()) {
                if (value.equals(Towncraft.NONE_KEY)) return true;
            }
            return false;
        }

        public void addCompleted(Key key) {
            int i = 0;
            for (Key value : completed.values()) {
                if (value.equals(Towncraft.NONE_KEY)) {
                    completed.put(i, key);
                    return;
                }
                i++;
            }
        }

        public boolean canAddCompleted() {
            return completed.containsValue(Towncraft.NONE_KEY);
        }

        public boolean canAddWaitingOrWorkingOn() {
            return canSetWorkingOn() || canAddWaiting();
        }

        public boolean canSetWorkingOn() {
            return workingOn.equals(Towncraft.NONE_KEY);
        }
    }
}
