package me.webhead1104.township.features.factories;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.dataLoaders.DataLoader;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FactoryType implements DataLoader {
    private static final Map<Key, Factory> factories = new HashMap<>();
    private static final Map<Key, Recipe> recipes = new HashMap<>();

    public static Collection<Factory> factoryValues() {
        return factories.values();
    }

    public static Collection<Key> factoryKeys() {
        return factories.keySet();
    }

    public static Factory getFactory(Key key) {
        return factories.get(key);
    }

    public static Recipe getRecipe(Key key) {
        return recipes.get(key);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        try {
            List<Factory> factories = new ArrayList<>(getListFromMultipleFiles("/data/factories", Factory.class));
            List<Recipe> recipes = new ArrayList<>();
            for (Factory factory : factories) {
                FactoryType.factories.put(factory.key(), factory);
                recipes.addAll(factory.getRecipes());
            }

            Recipe noneRecipe = new Recipe();
            noneRecipe.key = Township.noneKey;
            recipes.add(noneRecipe);

            for (Recipe recipe : recipes) {
                FactoryType.recipes.put(recipe.getKey(), recipe);
            }

            Township.logger.info("Loaded {} factories in {} ms!", FactoryType.factories.size(), System.currentTimeMillis() - start);

        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading factories! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Factory implements Keyed {
        @Setting("key")
        @Getter(value = AccessLevel.NONE)
        private Key key;
        @Setting("name")
        private String name;
        @Setting("recipes")
        private List<Recipe> recipes;
        @Setting("menu_title")
        private Component menuTitle;
        @Setting("building_key")
        private Key buildingKey;

        @PostProcess
        private void postProcess() {
            if (name == null) {
                name = Utils.thing2(key.value());
            }
            if (menuTitle == null) {
                menuTitle = Msg.format("<gold>%s", name);
            }
        }

        @Override
        public @NotNull Key key() {
            return key;
        }

        public boolean equals(Key key) {
            return Objects.equals(this.key, key);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Recipe {
        private final transient Map<ItemType.Item, Integer> ingredients = new HashMap<>();
        @Setting("key")
        private Key key;
        @Setting("material")
        private Material material;
        @Setting("result")
        private Key resultKey;
        private transient ItemType.Item result;
        @Setting("result_amount")
        private int resultAmount = 0;
        @Setting("ingredients")
        private Map<Key, Integer> ingredientKeys;
        @Setting("time")
        private Duration time;
        @Setting("level_needed")
        private int levelNeeded;
        @Setting("xp_given")
        private int xpGiven;

        @PostProcess
        private void postProcess() {
            result = ItemType.get(key);
            ingredientKeys.forEach((k, v) -> ingredients.put(ItemType.get(k), v));
        }

        public boolean hasRequiredItems(Barn barn) {
            AtomicBoolean hasRequiredItems = new AtomicBoolean(true);
            ingredients.forEach((key, amount) -> {
                if (barn.getItem(key) < amount) {
                    hasRequiredItems.set(false);
                }
            });
            return hasRequiredItems.get();
        }

        public boolean equals(Key key) {
            return Objects.equals(this.key, key);
        }
    }
}
