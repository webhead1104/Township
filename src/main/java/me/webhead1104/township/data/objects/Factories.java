package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@AllArgsConstructor
public class Factories {
    private final Map<FactoryType, Map<Integer, ItemType>> completed;
    private final Map<FactoryType, Map<Integer, RecipeType>> waiting;
    private final Map<FactoryType, RecipeType> working_on;
    private final Map<FactoryType, String> runnable_uuid;

    public static Factories createFactories() {
        Map<FactoryType, Map<Integer, ItemType>> completed = new HashMap<>();
        Map<FactoryType, Map<Integer, RecipeType>> waiting = new HashMap<>();
        Map<FactoryType, RecipeType> working_on = new HashMap<>();
        Map<FactoryType, String> runnable_uuid = new HashMap<>();
        for (FactoryType value : FactoryType.values()) {
            for (int i = 0; i < 3; ++i) {
                if (i == 0) {
                    completed.put(value, new HashMap<>(Map.of(i, ItemType.NONE)));
                    waiting.put(value, new HashMap<>(Map.of(i, RecipeType.NONE)));
                } else {
                    completed.get(value).put(i, ItemType.NONE);
                    waiting.get(value).put(i, RecipeType.NONE);
                }
            }
            working_on.put(value, RecipeType.NONE);
            runnable_uuid.put(value, "none");
        }
        return new Factories(completed, waiting, working_on, runnable_uuid);
    }

    public ItemType getCompleted(FactoryType type, int slot) {
        return completed.get(type).get(slot);
    }

    public void setCompleted(FactoryType type, int slot, ItemType value) {
        completed.get(type).put(slot, value);
    }

    public void addCompleted(FactoryType type, ItemType value) {
        for (int i = 0; i < 3; i++) {
            if (!completed.get(type).get(i).equals(ItemType.NONE)) continue;
            completed.get(type).put(i, value);
            return;
        }
    }

    public void workOnNext(FactoryType type) {
        setWorkingOn(type, getFirstWaiting(type));
        setFirstWaiting(type, RecipeType.NONE);
    }

    public RecipeType getWaiting(FactoryType type, int slot) {
        return waiting.get(type).get(slot);
    }

    public RecipeType getFirstWaiting(FactoryType type) {
        for (int i = 0; i < 3; i++) {
            if (!waiting.get(type).get(i).equals(RecipeType.NONE)) return waiting.get(type).get(i);
        }
        return RecipeType.NONE;
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

    public void addWaiting(FactoryType type, RecipeType value) {
        for (int i = 0; i < 3; i++) {
            if (waiting.get(type).get(i).equals(RecipeType.NONE)) continue;
            waiting.get(type).put(i, value);
        }
    }

    public RecipeType getWorkingOn(FactoryType type) {
        return working_on.get(type);
    }

    public void setWorkingOn(FactoryType type, RecipeType value) {
        working_on.put(type, value);
    }

    public String getRunnableUUID(FactoryType type) {
        return runnable_uuid.get(type);
    }

    public void setRunnableUUID(FactoryType type, String value) {
        runnable_uuid.put(type, value);
    }
}
