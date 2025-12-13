package me.webhead1104.towncraft.features.animals;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataLoaders.Keyed;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@DependsOn({ItemType.class})
public class AnimalType implements DataLoader.KeyBasedDataLoader<AnimalType.Animal> {
    private final Map<Key, Animal> values = new LinkedHashMap<>();

    public Animal get(Key key) {
        if (!values.containsKey(key)) {
            throw new IllegalStateException("Animal type does not exist! key:" + key.asString());
        }
        return values.get(key);
    }

    public Collection<Key> keys() {
        return values.keySet();
    }

    public Collection<Animal> values() {
        return values.values();
    }

    @Override
    public void load() {
        try {
            List<Animal> list = getListFromFile("/data/animals.json", Animal.class);
            for (Animal animal : list) {
                animal.postProcess();
                Key baseKey = animal.key();
                for (int i = 1; i <= animal.getAmount(); i++) {
                    Key key = Key.key(baseKey.asString() + "_" + i);
                    Animal instance = animal.withKey(key);
                    values.put(key, instance);
                }
            }
            Towncraft.getLogger().info("Loaded {} animals!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading animals! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @With
    @Getter
    @NoArgsConstructor
    @ConfigSerializable
    @AllArgsConstructor
    public static class Animal extends Keyed {
        @Required
        @Setting("key")
        private Key key;
        @Required
        @Setting
        private int amount;
        @Required
        @Setting("name")
        private String name;
        @Required
        @Setting("animal_name")
        private String animalName;
        @Required
        @Setting("material")
        private TowncraftMaterial animalMaterial;
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
        private transient TowncraftItemStack animalItemStack;
        private transient ItemType.Item feed;
        private transient ItemType.Item product;

        private void postProcess() {
            this.menuTitle = Msg.format("<gold>%s", name);
            this.animalItemStack = Utils.getItemStack(animalName, animalMaterial);
            this.feed = Towncraft.getDataLoader(ItemType.class).get(feedKey);
            this.product = Towncraft.getDataLoader(ItemType.class).get(productKey);
        }
    }
}
