package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;
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

    @Getter
    @Setter
    @ConfigSerializable
    public static class Factory {
        private final Map<Integer, RecipeType> waiting = new HashMap<>();
        private final Map<Integer, ItemType> completed = new HashMap<>();
        private RecipeType workingOn;
        private Instant instant;

        public Factory() {
            for (int i = 0; i < 3; i++) {
                this.waiting.put(i, RecipeType.NONE);
                this.completed.put(i, ItemType.NONE);
            }
            this.workingOn = RecipeType.NONE;
            this.instant = Instant.EPOCH;
        }

        public RecipeType getWaiting(int slot) {
            return waiting.get(slot);
        }

        public RecipeType removeFirstWaiting() {
            RecipeType recipeType = waiting.get(0);
            for (int i = 1; i < 3; i++) {
                waiting.put(i - 1, waiting.get(i));
            }
            waiting.put(2, RecipeType.NONE);

            return recipeType;
        }

        public void addWaiting(RecipeType recipeType) {
            for (int i = 0; i < 3; i++) {
                if (!waiting.get(i).equals(RecipeType.NONE)) continue;
                waiting.put(i, recipeType);
                return;
            }
        }

        public boolean hasWaiting() {
            for (RecipeType value : waiting.values()) {
                if (!value.equals(RecipeType.NONE)) return true;
            }
            return false;
        }

        public boolean canAddWaiting() {
            for (RecipeType value : waiting.values()) {
                if (value.equals(RecipeType.NONE)) return true;
            }
            return false;
        }

        public ItemType getCompleted(int slot) {
            return completed.get(slot);
        }

        public void setCompleted(int slot, ItemType value) {
            completed.put(slot, value);
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
            return workingOn.equals(RecipeType.NONE);
        }
    }
}
