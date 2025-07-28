package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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
        private final Map<Integer, RecipeType> waiting = new HashMap<>();
        private final Map<Integer, ItemType> completed = new HashMap<>();
        private RecipeType workingOn;
        private boolean unlocked;
        private Instant instant;

        public Factory() {
            for (int i = 0; i < 3; i++) {
                this.waiting.put(i, RecipeType.NONE);
                this.completed.put(i, ItemType.NONE);
            }
            this.workingOn = RecipeType.NONE;
            this.unlocked = false;
            this.instant = Instant.EPOCH;
        }

        public RecipeType getWaiting(int slot) {
            return waiting.get(slot);
        }

        public void setWaiting(int slot, RecipeType waiting) {
            this.waiting.put(slot, waiting);
        }

        public boolean hasWaiting() {
            AtomicBoolean good = new AtomicBoolean(false);
            waiting.forEach((key, value) -> {
                if (!value.equals(RecipeType.NONE)) {
                    good.set(true);
                }
            });
            return good.get();
        }

        public int getFirstWaiting() {
            for (int i = 2; i > -1; i--) {
                if (!waiting.get(i).equals(RecipeType.NONE)) return i;
            }
            return -1;
        }

        public boolean canAddWaiting() {
            return waiting.containsValue(RecipeType.NONE);
        }

        public int addWaiting(RecipeType recipeType) {
            int i = 0;
            for (RecipeType value : waiting.values()) {
                if (value.equals(RecipeType.NONE)) {
                    waiting.put(i, recipeType);
                    break;
                }
                i++;
            }
            return i;
        }

        public ItemType getCompleted(int slot) {
            return completed.get(slot);
        }

        public void setCompleted(int slot, ItemType value) {
            completed.put(slot, value);
        }

        public int addCompleted(ItemType itemType) {
            int i = 0;
            for (ItemType value : completed.values()) {
                if (value.equals(ItemType.NONE)) {
                    completed.put(i, itemType);
                    return i;
                }
                i++;
            }
            return -1;
        }

        public boolean canStartWorking() {
            return workingOn.equals(RecipeType.NONE) || waiting.containsValue(RecipeType.NONE);
        }
    }
}
