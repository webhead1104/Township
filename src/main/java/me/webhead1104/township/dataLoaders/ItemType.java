package me.webhead1104.township.dataLoaders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.Keyed;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.*;

public class ItemType implements DataLoader.KeyBasedDataLoader<ItemType.Item> {
    public final Map<Key, Item> values = new HashMap<>();

    public Item get(Key key) {
        return values.get(key);
    }

    public Collection<Key> keys() {
        return values.keySet();
    }

    public Collection<Item> values() {
        return values.values();
    }

    @Override
    public void load() {
        try {
            List<Item> list = getListFromFile("/data/items.json", Item.class);
            for (Item item : list) {
                item.postProcess();
                values.put(item.key(), item);
            }
            Township.logger.info("Loaded {} items!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading items! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Item implements Keyed {
        @Required
        @Getter(value = AccessLevel.NONE)
        @Setting("key")
        private Key key;
        @Required
        @Setting("material")
        private Material material;
        @Required
        @Setting("sell_price")
        private int sellPrice;
        @Required
        @Setting("xp_given")
        private int xpGiven;
        private transient ItemStack itemStack;
        private transient String name;

        private void postProcess() {
            this.name = Utils.thing2(key.value());
            this.itemStack = Utils.getItemStack(name, material);
        }

        public ItemStack getItemStack() {
            return itemStack.clone();
        }

        public boolean equals(Key key) {
            return Objects.equals(this.key, key);
        }

        @Override
        public @NotNull Key key() {
            return key;
        }
    }
}
