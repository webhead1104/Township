package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.datafixer.TownshipCodecs;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.enums.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class Factories {
    public static final @NotNull Codec<Factories> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(FactoryType.CODEC, Factory.CODEC).fieldOf("factories").forGetter(Factories::getFactories)
    ).apply(instance, Factories::new));
    private final Map<FactoryType, Factory> factories = new HashMap<>();

    public Factories() {
        for (FactoryType value : FactoryType.values()) {
            factories.put(value, new Factory());
        }
    }

    public Factories(Map<FactoryType, Factory> factories) {
        this.factories.putAll(factories);
    }

    public Factory getFactory(FactoryType factoryType) {
        return factories.get(factoryType);
    }

    public void setFactory(FactoryType factoryType, Factory factory) {
        factories.put(factoryType, factory);
    }

    @Getter
    @Setter
    public static class Factory {
        public static final @NotNull Codec<Factory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(TownshipCodecs.INT, RecipeType.CODEC).fieldOf("waiting").forGetter(Factory::getWaiting),
                Codec.unboundedMap(TownshipCodecs.INT, ItemType.CODEC).fieldOf("completed").forGetter(Factory::getCompleted),
                RecipeType.CODEC.fieldOf("workingOn").forGetter(Factory::getWorkingOn),
                Codec.BOOL.fieldOf("unlocked").forGetter(Factory::isUnlocked),
                TownshipCodecs.INSTANT.fieldOf("instant").forGetter(Factory::getInstant)
        ).apply(instance, Factory::new));
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

        public Factory(Map<Integer, RecipeType> waiting, Map<Integer, ItemType> completed, RecipeType workingOn, boolean unlocked, Instant instant) {
            this.waiting.putAll(waiting);
            this.completed.putAll(completed);
            this.workingOn = workingOn;
            this.unlocked = unlocked;
            this.instant = instant;
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
