package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.features.factories.FactoryType;
import me.webhead1104.township.features.factories.RecipeType;
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
        for (FactoryType.Factory value : Township.getDataLoader(FactoryType.class).values()) {
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
                this.waiting.put(i, Township.noneKey);
                this.completed.put(i, Township.noneKey);
            }
            this.workingOn = Township.noneKey;
            this.instant = Instant.EPOCH;
        }

        public RecipeType.Recipe getWaiting(int slot) {
            return Township.getDataLoader(RecipeType.class).get(waiting.get(slot));
        }

        public void setCompleted(int slot, Key item) {
            completed.put(slot, item);
        }

        public RecipeType.Recipe getCompleted(int slot) {
            return Township.getDataLoader(RecipeType.class).get(completed.get(slot));
        }

        public RecipeType.Recipe getWorkingOn() {
            return Township.getDataLoader(RecipeType.class).get(workingOn);
        }

        public RecipeType.Recipe removeFirstWaiting() {
            RecipeType.Recipe recipe = getWaiting(0);
            for (int i = 1; i < 3; i++) {
                waiting.put(i - 1, waiting.get(i));
            }
            waiting.put(2, Township.noneKey);

            return recipe;
        }

        public void addWaiting(Key key) {
            for (int i = 0; i < 3; i++) {
                if (!waiting.get(i).equals(Township.noneKey)) continue;
                waiting.put(i, key);
                return;
            }
        }

        public boolean hasWaiting() {
            for (Key value : waiting.values()) {
                if (!value.equals(Township.noneKey)) return true;
            }
            return false;
        }

        public boolean canAddWaiting() {
            for (Key value : waiting.values()) {
                if (value.equals(Township.noneKey)) return true;
            }
            return false;
        }

        public void addCompleted(Key key) {
            int i = 0;
            for (Key value : completed.values()) {
                if (value.equals(Township.noneKey)) {
                    completed.put(i, key);
                    return;
                }
                i++;
            }
        }

        public boolean canAddCompleted() {
            return completed.containsValue(Township.noneKey);
        }

        public boolean canAddWaitingOrWorkingOn() {
            return canSetWorkingOn() || canAddWaiting();
        }

        public boolean canSetWorkingOn() {
            return workingOn.equals(Township.noneKey);
        }
    }
}
