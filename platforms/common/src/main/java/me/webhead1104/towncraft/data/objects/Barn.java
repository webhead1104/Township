package me.webhead1104.towncraft.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.ItemType;
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
        for (ItemType.Item type : Towncraft.getDataLoader(ItemType.class).values()) {
            if (type.equals(Towncraft.NONE_KEY)) continue;
            if (type.equals(Towncraft.key("cow_feed"))) {
                itemMap.put(type.key(), 12);
                continue;
            }
            if (type.equals(Towncraft.key("chicken_feed"))) {
                itemMap.put(type.key(), 12);
                continue;
            }
            if (type.equals(Towncraft.key("paint"))) {
                itemMap.put(type.key(), 20);
                continue;
            }
            if (type.equals(Towncraft.key("nail"))) {
                itemMap.put(type.key(), 20);
                continue;
            }
            if (type.equals(Towncraft.key("hammer"))) {
                itemMap.put(type.key(), 20);
            }
        }
    }

    public int getItem(Key key) {
        remove0Item(key);
        return itemMap.getOrDefault(key, 0);
    }

    public void setItem(Key key, int value) {
        itemMap.put(key, value);
        remove0Item(key);
    }

    public void addAmountToItem(Key key, int amount) {
        itemMap.put(key, getItem(key) + amount);
        remove0Item(key);
    }

    public void removeAmountFromItem(Key key, int amount) {
        itemMap.put(key, getItem(key) - amount);
        remove0Item(key);
    }

    private void remove0Item(Key key) {
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
