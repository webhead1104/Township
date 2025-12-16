package me.webhead1104.towncraft.features.factories;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.data.objects.Barn;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.PostProcess;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@DependsOn({ItemType.class, FactoryType.class})
public class RecipeType implements DataLoader.KeyBasedDataLoader<RecipeType.Recipe> {
    private final Map<Key, Recipe> values = new HashMap<>();

    public Recipe get(Key key) {
        if (!values.containsKey(key)) {
            throw new IllegalStateException("Recipe type does not exist! key:" + key.asString());
        }
        return values.get(key);
    }

    @Override
    public Collection<Key> keys() {
        return values.keySet();
    }

    @Override
    public Collection<Recipe> values() {
        return values.values();
    }

    @Override
    public void load() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        try {
            for (FactoryType.Factory factory : Towncraft.getDataLoader(FactoryType.class).values()) {
                for (Recipe recipe : factory.getRecipes()) {
                    values.put(recipe.key(), recipe);
                }
            }

            Recipe noneRecipe = new Recipe();
            noneRecipe.key = Towncraft.NONE_KEY;
            values.put(noneRecipe.key(), noneRecipe);

            Towncraft.getLogger().info("Loaded {} recipes in {}ms!", values.size(), stopwatch.stop().elapsed().toMillis());

        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading factories! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Recipe extends Keyed {
        private final transient Map<ItemType.Item, Integer> ingredients = new HashMap<>();
        @Required
        @Setting("key")
        private Key key;
        @Required
        @Setting("result_amount")
        private int resultAmount;
        @Required
        @Setting("ingredients")
        private Map<Key, Integer> ingredientKeys;
        @Required
        @Setting("time")
        private Duration time;
        @Required
        @Setting("level_needed")
        private int levelNeeded;
        @Required
        @Setting("xp_given")
        private int xpGiven;
        private transient ItemType.Item result;

        @PostProcess
        private void postProcess() {
            result = Towncraft.getDataLoader(ItemType.class).get(key);
            ingredientKeys.forEach((k, v) -> ingredients.put(Towncraft.getDataLoader(ItemType.class).get(k), v));
            if (resultAmount == 0) {
                resultAmount = 1;
            }
        }

        public boolean hasRequiredItems(Barn barn) {
            AtomicBoolean hasRequiredItems = new AtomicBoolean(true);
            ingredients.forEach((item, amount) -> {
                if (barn.getItem(item.key()) < amount) {
                    hasRequiredItems.set(false);
                }
            });
            return hasRequiredItems.get();
        }
    }
}
