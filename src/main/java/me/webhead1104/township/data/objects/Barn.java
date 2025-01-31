package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Barn {

    private final Map<ItemType, Integer> itemMap;
    private BarnUpgrade barnUpgrade;

    public static Barn createItems() {
        try {
            Map<ItemType, Integer> map = new HashMap<>();
            for (ItemType type : ItemType.values()) {
                if (type == ItemType.NONE) continue;
                if (type == ItemType.COW_FEED) {
                    map.put(type, 12);
                    continue;
                }
                if (type == ItemType.CHICKEN_FEED) {
                    map.put(type, 12);
                    continue;
                }
                if (type.equals(ItemType.PAINT)) {
                    map.put(type, 20);
                    continue;
                }
                if (type.equals(ItemType.NAIL)) {
                    map.put(type, 20);
                    continue;
                }
                if (type.equals(ItemType.HAMMER)) {
                    map.put(type, 20);
                    continue;
                }
                map.put(type, 0);
            }

            return new Barn(map, new BarnUpgrade(1, 2, 70));
        } catch (Exception e) {
            Township.logger.error("error", e);
        }
        return createItems();
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
