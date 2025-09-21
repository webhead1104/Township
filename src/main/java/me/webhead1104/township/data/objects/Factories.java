package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.dataLoaders.RecipeDataLoader;
import me.webhead1104.township.features.factories.FactoryType;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigSerializable
public class Factories {
    private final Map<FactoryType, Factory> factoryBuildings = new HashMap<>();

    public Factories() {
        for (FactoryType value : FactoryType.values()) {
            factoryBuildings.put(value, new Factory());
        }
    }

    public Factory getFactory(FactoryType factoryType) {
        return factoryBuildings.get(factoryType);
    }


    @ConfigSerializable
    public static class Factory {
        private final Map<Integer, Key> waiting = new HashMap<>();
        private final Map<Integer, ItemType> completed = new HashMap<>();
        private Key workingOn;
        @Getter
        @Setter
        private Instant instant;

        public Factory() {
            for (int i = 0; i < 3; i++) {
                this.waiting.put(i, Township.noneKey);
                this.completed.put(i, ItemType.NONE);
            }
            this.workingOn = Township.noneKey;
            this.instant = Instant.EPOCH;
        }

        public RecipeDataLoader.Recipe getWaiting(int slot) {
            return RecipeDataLoader.get(waiting.get(slot));
        }

        public Key getRealWaiting(int slot) {
            return waiting.get(slot);
        }

        public void setCompleted(int slot, ItemType item) {
            completed.put(slot, item);
        }

        public ItemType getCompleted(int slot) {
            return completed.get(slot);
        }

        public RecipeDataLoader.Recipe getWorkingOn() {
            return RecipeDataLoader.get(workingOn);
        }

        public void setWorkingOn(RecipeDataLoader.Recipe workingOn) {
            setRealWorkingOn(workingOn.getKey());
        }

        public Key getRealWorkingOn() {
            return workingOn;
        }

        public void setRealWorkingOn(Key workingOn) {
            this.workingOn = workingOn;
        }

        public RecipeDataLoader.Recipe removeFirstWaiting() {
            RecipeDataLoader.Recipe recipe = getWaiting(0);
            for (int i = 1; i < 3; i++) {
                waiting.put(i - 1, waiting.get(i));
            }
            waiting.put(2, Township.noneKey);

            return recipe;
        }

        public void addWaiting(RecipeDataLoader.Recipe recipe) {
            addWaiting(recipe.getKey());
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

        public void addCompleted(ItemType itemType) {
            int i = 0;
            for (ItemType value : completed.values()) {
                if (value.equals(ItemType.NONE)) {
                    completed.put(i, itemType);
                    return;
                }
                i++;
            }
        }

        public boolean canAddCompleted() {
            return completed.containsValue(ItemType.NONE);
        }

        public boolean canAddWaitingOrWorkingOn() {
            return canSetWorkingOn() || canAddWaiting();
        }

        public boolean canSetWorkingOn() {
            return workingOn.equals(Township.noneKey);
        }
    }
}
