package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.ItemType;
import net.kyori.adventure.key.Key;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigSerializable
public class Barn {
    private final Map<Key, Integer> itemMap = new HashMap<>();
    private BarnUpgrade barnUpgrade = new BarnUpgrade(1, 2, 70);

    public Barn() {
        for (ItemType.Item type : ItemType.values()) {
            if (type.equals(Township.noneKey)) continue;
            if (type.equals(Township.key("cow_feed"))) {
                itemMap.put(type.getKey(), 12);
                continue;
            }
            if (type.equals(Township.key("chicken_feed"))) {
                itemMap.put(type.getKey(), 12);
                continue;
            }
            if (type.equals(Township.key("paint"))) {
                itemMap.put(type.getKey(), 20);
                continue;
            }
            if (type.equals(Township.key("nail"))) {
                itemMap.put(type.getKey(), 20);
                continue;
            }
            if (type.equals(Township.key("hammer"))) {
                itemMap.put(type.getKey(), 20);
            }
        }
    }

    public int getItem(ItemType.Item item) {
        return getItem(item.getKey());
    }

    public int getItem(Key key) {
        remov0Item(key);
        return itemMap.getOrDefault(key, 0);
    }

    public void setItem(ItemType.Item item, int amount) {
        setItem(item.getKey(), amount);
    }

    public void setItem(Key key, int value) {
        itemMap.put(key, value);
        remov0Item(key);
    }

    public void addAmountToItem(ItemType.Item item, int amount) {
        addAmountToItem(item.getKey(), amount);
    }

    public void addAmountToItem(Key key, int amount) {
        itemMap.put(key, getItem(key) + amount);
        remov0Item(key);
    }

    public void removeAmountFromItem(ItemType.Item item, int amount) {
        removeAmountFromItem(item.getKey(), amount);
    }

    public void removeAmountFromItem(Key key, int amount) {
        itemMap.put(key, getItem(key) - amount);
        remov0Item(key);
    }

    private void remov0Item(Key key) {
        if (!itemMap.containsKey(key)) return;
        if (itemMap.get(key) <= 0) {
            itemMap.remove(key);
        }
    }

    public int getStorage() {
        int storage = 0;
        for (int value : itemMap.values()) storage += value;
        return storage;
    }
}
