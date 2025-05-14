package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.ItemType;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigSerializable
public class Barn {
    private final Map<ItemType, Integer> itemMap = new HashMap<>();
    private BarnUpgrade barnUpgrade = new BarnUpgrade(1, 2, 70);

    public Barn() {
        for (ItemType type : ItemType.values()) {
            if (type == ItemType.NONE) continue;
            if (type == ItemType.COW_FEED) {
                itemMap.put(type, 12);
                continue;
            }
            if (type == ItemType.CHICKEN_FEED) {
                itemMap.put(type, 12);
                continue;
            }
            if (type.equals(ItemType.PAINT)) {
                itemMap.put(type, 20);
                continue;
            }
            if (type.equals(ItemType.NAIL)) {
                itemMap.put(type, 20);
                continue;
            }
            if (type.equals(ItemType.HAMMER)) {
                itemMap.put(type, 20);
                continue;
            }
            itemMap.put(type, 0);
        }
    }

    public int getItem(ItemType type) {
        return itemMap.get(type);
    }

    public void setItem(ItemType type, int value) {
        itemMap.put(type, value);
    }

    public void addAmountToItem(ItemType type, int amount) {
        itemMap.put(type, getItem(type) + amount);
    }

    public void removeAmountFromItem(ItemType type, int amount) {
        itemMap.put(type, getItem(type) - amount);
    }

    public int getStorage() {
        int storage = 0;
        for (int value : itemMap.values()) storage += value;
        return storage;
    }
}
