package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import me.webhead1104.township.data.enums.AnimalType;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@AllArgsConstructor
public class Animals {
    private final Map<AnimalType, Map<Integer, Boolean>> feed;
    private final Map<AnimalType, Map<Integer, Boolean>> product;
    private final Map<AnimalType, Map<Integer, Boolean>> unlocked;
    private final Map<AnimalType, Map<Integer, String>> runnable_uuid;

    public static Animals createAnimals() {
        Map<AnimalType, Map<Integer, Boolean>> feed = new HashMap<>();
        Map<AnimalType, Map<Integer, Boolean>> product = new HashMap<>();
        Map<AnimalType, Map<Integer, Boolean>> unlocked = new HashMap<>();
        Map<AnimalType, Map<Integer, String>> runnable_uuid = new HashMap<>();
        for (AnimalType value : AnimalType.values()) {
            for (int i = 0; i < 6; i++) {
                feed.put(value, new HashMap<>(Map.of(i, false)));
                product.put(value, new HashMap<>(Map.of(i, false)));
                unlocked.put(value, new HashMap<>(Map.of(i, false)));
                runnable_uuid.put(value, new HashMap<>(Map.of(i, "none")));
            }
        }
        return new Animals(feed, product, unlocked, runnable_uuid);
    }

    public void setFeed(AnimalType type, int animal, boolean value) {
        feed.get(type).put(animal, value);
    }

    public boolean getFeed(AnimalType type, int animal) {
        return feed.get(type).getOrDefault(animal, false);
    }

    public void setProduct(AnimalType type, int animal, boolean value) {
        product.get(type).put(animal, value);
    }

    public boolean getProduct(AnimalType type, int animal) {
        return product.get(type).getOrDefault(animal, false);
    }

    public void setUnlocked(AnimalType type, int animal, boolean value) {
        unlocked.get(type).put(animal, value);
    }

    public boolean isUnlocked(AnimalType type, int animal) {
        return unlocked.get(type).getOrDefault(animal, false);
    }

    public void setRunnableUUID(AnimalType type, int animal, String value) {
        runnable_uuid.put(type, Map.of(animal, value));
    }

    public String getRunnableUUID(AnimalType type, int animal) {
        return runnable_uuid.get(type).getOrDefault(animal, "");
    }
}