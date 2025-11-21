package me.webhead1104.towncraft.dataLoaders;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemType implements DataLoader.KeyBasedDataLoader<ItemType.Item> {
    private final Map<Key, Item> values = new HashMap<>();

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
            Towncraft.logger.info("Loaded {} items!", values.size());
        } catch (Exception e) {
            throw new RuntimeException("An error occurred whilst loading items! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    @Getter
    @ConfigSerializable
    @NoArgsConstructor
    public static class Item extends Keyed {
        @Required
        @Setting("key")
        private Key key;
        @Required
        @Setting("material")
        private Material material;
        @Required
        @Setting("sell_price")
        private int sellPrice;
        @Required
        @Setting("level_needed")
        private int levelNeeded;
        private transient ItemStack itemStack;
        private transient String name;

        private void postProcess() {
            this.name = Utils.thing2(key.value());
            this.itemStack = Utils.getItemStack(name, material);
        }

        public ItemStack getItemStack() {
            return itemStack.clone();
        }
    }
}
