package me.webhead1104.township.features.animals;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.DataLoader;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
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
                values.put(animal.getKey(), animal);
            }
            Township.logger.info("Loaded {} animals!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading animals! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Animal {
        @Setting("key")
        private Key key;
        @Setting("name")
        private String name;
        @Setting("animal_name")
        private String animalName;
        @Setting("material")
        private Material animalMaterial;
        private transient ItemStack animalItemStack;
        @Setting("feed")
        private Key feedKey;
        private transient ItemType.Item feed;
        @Setting("product")
        private Key productKey;
        private transient ItemType.Item product;
        @Setting("xp_claim")
        private int claimXp;
        @Setting("time")
        private Duration time;
        @Setting("building_key")
        private Key buildingKey;
        private transient Component menuTitle;

        private void postProcess() {
            this.animalItemStack = Utils.getItemStack(animalName, animalMaterial);
            this.feed = ItemType.get(feedKey);
            this.product = ItemType.get(productKey);
            this.menuTitle = Msg.format("<gold>%s", name);
        }

        public ItemStack getItemStack() {
            return animalItemStack.clone();
        }

        public boolean equals(Key key) {
            return Objects.equals(this.key, key);
        }
    }
}
