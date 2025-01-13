package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
@AllArgsConstructor
public class Factories {
    public final Map<FactoryType, Map<Integer, RecipeType>> waiting;
    private final Map<FactoryType, Map<Integer, ItemType>> completed;
    private final Map<FactoryType, RecipeType> working_on;
    private final Map<FactoryType, Boolean> unlocked;
    private final Map<FactoryType, Instant> instant;

    public static Factories createFactories() {
        Map<FactoryType, Map<Integer, ItemType>> completed = new HashMap<>();
        Map<FactoryType, Map<Integer, RecipeType>> waiting = new HashMap<>();
        Map<FactoryType, RecipeType> working_on = new HashMap<>();
        Map<FactoryType, Boolean> unlocked = new HashMap<>();
        Map<FactoryType, Instant> instant = new HashMap<>();
        for (FactoryType value : FactoryType.values()) {
            for (int i = 0; i < 3; ++i) {
                if (i == 0) {
                    completed.put(value, new HashMap<>());
                    waiting.put(value, new HashMap<>());
                }
                completed.get(value).put(i, ItemType.NONE);
                waiting.get(value).put(i, RecipeType.NONE);
            }
            working_on.put(value, RecipeType.NONE);
            unlocked.put(value, false);
            instant.put(value, Instant.EPOCH);
        }
        return new Factories(waiting, completed, working_on, unlocked, instant);
    }

    public ItemType getCompleted(FactoryType type, int slot) {
        return completed.get(type).get(slot);
    }

    public void setCompleted(FactoryType type, int slot, ItemType value) {
        completed.get(type).put(slot, value);
    }

    public boolean canAddCompleted(FactoryType factoryType) {
        return completed.get(factoryType).containsValue(ItemType.NONE);
    }

    public int addCompleted(FactoryType factoryType, ItemType itemType) {
        int i = 0;
        for (ItemType value : completed.get(factoryType).values()) {
            if (value.equals(ItemType.NONE)) {
                completed.get(factoryType).put(i, itemType);
                return i;
            }
            i++;
        }
        return -1;
    }

    public RecipeType getWaiting(FactoryType type, int slot) {
        return waiting.get(type).get(slot);
    }

    public int getFirstWaiting(FactoryType type) {
        for (int i = 2; i > -1; i--) {
            if (!waiting.get(type).get(i).equals(RecipeType.NONE)) return i;
        }
        return -1;
    }

    public void setFirstWaiting(FactoryType factoryType, RecipeType recipeType) {
        for (int i = 0; i < 3; i++) {
            if (!waiting.get(factoryType).get(i).equals(RecipeType.NONE)) continue;
            setWaiting(factoryType, i, recipeType);
        }
    }

    public void setWaiting(FactoryType type, int slot, RecipeType value) {
        waiting.get(type).put(slot, value);
    }

    public boolean hasWaiting(FactoryType type) {
        AtomicBoolean good = new AtomicBoolean(false);
        waiting.get(type).forEach((key, value) -> {
            if (!value.equals(RecipeType.NONE)) {
                good.set(true);
            }
        });
        return good.get();
    }

    public int addWaiting(FactoryType factoryType, RecipeType recipeType) {
        int i = 0;
        for (RecipeType value : waiting.get(factoryType).values()) {
            if (value.equals(RecipeType.NONE)) {
                waiting.get(factoryType).put(i, recipeType);
                break;
            }
            i++;
        }
        return i;
    }

    public boolean canAddWaiting(FactoryType factoryType) {
        return waiting.get(factoryType).containsValue(RecipeType.NONE);
    }

    public RecipeType getWorkingOn(FactoryType type) {
        return working_on.getOrDefault(type, RecipeType.NONE);
    }

    public boolean canStartWorking(FactoryType type) {
        return working_on.get(type).equals(RecipeType.NONE) || waiting.get(type).containsValue(RecipeType.NONE);
    }

    public void setWorkingOn(FactoryType type, RecipeType value) {
        working_on.put(type, value);
    }

    public Instant getInstant(FactoryType type) {
        return instant.get(type);
    }

    public void setInstant(FactoryType type, Instant value) {
        instant.put(type, value);
    }

    public boolean isUnlocked(FactoryType type) {
        return unlocked.get(type);
    }

    public void setUnlocked(FactoryType type, boolean isUnlocked) {
        unlocked.put(type, isUnlocked);
    }
}
