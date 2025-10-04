package me.webhead1104.township.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.township.Township;
import me.webhead1104.township.utils.Utils;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ItemType implements DataLoader {
    public static final Map<Key, Item> values = new HashMap<>();

    public static Collection<Item> values() {
        return values.values();
    }

    public static Collection<Key> keys() {
        return values.keySet();
    }

    public static Item get(Key key) {
        return values.get(key);
    }

    @Override
    public void load() {
        try {
            long start = System.currentTimeMillis();
            ConfigurationNode node = getNodeFromFile("/data/items.json");
            var nodeList = node.getList(Item.class);
            if (nodeList == null || nodeList.isEmpty()) {
                throw new RuntimeException("No items found!");
            }
            for (Item item : nodeList) {
                item.postProcess();
                values.put(item.getKey(), item);
            }
            Township.logger.info("Loaded {} items in {} ms!", values.size(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading items! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Item {
        @Setting("key")
        private Key key;
        @Setting("material")
        private Material material;
        @Setting("sell_price")
        private int sellPrice;
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
    }
}
