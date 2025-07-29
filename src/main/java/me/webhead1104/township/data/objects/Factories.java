package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public void setFactory(FactoryType factoryType, Factory factory) {
        factoryBuildings.put(factoryType, factory);
    }

    @Getter
    @Setter
    @ConfigSerializable
    public static class Factory {
        private final List<RecipeType> waiting = new ArrayList<>();
        private final Map<Integer, ItemType> completed = new HashMap<>();
        private RecipeType workingOn;
        private boolean unlocked;
        private Instant instant;

        public Factory() {
            for (int i = 0; i < 3; i++) {
                this.waiting.add(i, RecipeType.NONE);
                this.completed.put(i, ItemType.NONE);
            }
            this.workingOn = RecipeType.NONE;
            this.unlocked = false;
            this.instant = Instant.EPOCH;
        }

        public RecipeType getWaiting(int slot) {
            return waiting.get(slot);
        }

        public RecipeType removeFirstWaiting() {
            return waiting.removeFirst();
        }

        public void addWaiting(RecipeType recipeType) {
            waiting.addLast(recipeType);
        }

        public boolean hasWaiting() {
            return !waiting.isEmpty();
        }

        public boolean canAddWaiting() {
            return waiting.size() < 3;
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
            return workingOn.equals(RecipeType.NONE) || waiting.size() < 3;
        }

        public boolean canSetWorkingOn() {
            return workingOn.equals(RecipeType.NONE);
        }
    }
}
