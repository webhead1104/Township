package me.webhead1104.township.features.animals;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.DataLoader;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.*;

public class AnimalType implements DataLoader {
    public static final Map<Key, Animal> values = new HashMap<>();

    public static Collection<Animal> values() {
        return values.values();
    }

    public static Collection<Key> keys() {
        return values.keySet();
    }

    public static Animal get(Key key) {
        return values.get(key);
    }

    @Override
    public void load() {
        try {
            List<Animal> list = getListFromFile("/data/animals.json", Animal.class);
            for (Animal animal : list) {
                animal.postProcess();
                values.put(animal.key(), animal);
            }
            Township.logger.info("Loaded {} animals!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading animals! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Animal implements Keyed {
        @Required
        @Getter(value = AccessLevel.NONE)
        @Setting("key")
        private Key key;
        @Required
        @Setting("name")
        private String name;
        @Required
        @Setting("animal_name")
        private String animalName;
        @Required
        @Setting("material")
        private Material animalMaterial;
        @Required
        @Setting("feed")
        private Key feedKey;
        @Required
        @Setting("product")
        private Key productKey;
        @Required
        @Setting("xp_claim")
        private int claimXp;
        @Required
        @Setting("time")
        private Duration time;
        @Required
        @Setting("building_key")
        private Key buildingKey;
        private transient Component menuTitle;
        private transient ItemStack animalItemStack;
        private transient ItemType.Item feed;
        private transient ItemType.Item product;

        private void postProcess() {
            this.animalItemStack = Utils.getItemStack(animalName, animalMaterial);
            this.feed = ItemType.get(feedKey);
            this.product = ItemType.get(productKey);
            this.menuTitle = Msg.format("<gold>%s", name);
        }

        @Override
        public @NotNull Key key() {
            return key;
        }

        public ItemStack getItemStack() {
            return animalItemStack.clone();
        }

        public boolean equals(Key key) {
            return Objects.equals(this.key, key);
        }
    }
}
