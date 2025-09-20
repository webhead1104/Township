package me.webhead1104.township.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RecipeDataLoader implements DataLoader {
    @Getter
    private static final Map<Key, Recipe> map = new HashMap<>();

    public static Recipe get(Key key) {
        return map.get(key);
    }

    @Override
    public void load() {
        long start = System.currentTimeMillis();
        try {
            List<Recipe> recipes = new ArrayList<>();

            String jarPath = RecipeDataLoader.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath();

            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

            try (JarFile jar = new JarFile(jarPath)) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();

                    if (!entry.isDirectory()
                            && entry.getName().startsWith("data/recipes/")
                            && entry.getName().endsWith(".json")) {

                        Township.logger.debug("Loading recipe file: {}", entry.getName());

                        try (InputStream inputStream = jar.getInputStream(entry);
                             BufferedReader reader = new BufferedReader(
                                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER
                                    .source(() -> reader)
                                    .build()
                                    .load();

                            List<Recipe> fileRecipes = node.getList(Recipe.class);
                            if (fileRecipes != null) {
                                recipes.addAll(fileRecipes);
                                Township.logger.debug("Loaded {} recipes from {}", fileRecipes.size(), entry.getName());
                            }
                        }
                    }
                }
            }

            for (Recipe recipe : recipes) {
                if (recipe.getKey() != null) {
                    map.put(recipe.getKey(), recipe);
                } else {
                    Township.logger.warn("Found recipe with null key, skipping...");
                }
            }

            Township.logger.info("Loaded {} recipes in {} ms!", map.size(), System.currentTimeMillis() - start);

        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading recipes! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Recipe {
        @Setting("key")
        private Key key;
        @Setting("material")
        private Material material;
        @Setting("result")
        private ItemDataLoader.Item result = ItemDataLoader.get(key);
        @Setting("result_amount")
        private int resultAmount = 0;
        @Setting("ingredients")
        private Map<ItemDataLoader.Item, Integer> ingredients;
        @Setting("time")
        private Duration time;
        @Setting("level_needed")
        private int levelNeeded;
        @Setting("xp_given")
        private int xpGiven;
    }
}
