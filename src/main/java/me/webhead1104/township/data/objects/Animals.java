package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import me.webhead1104.township.data.enums.AnimalType;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@AllArgsConstructor
public class Animals {
    private final Map<AnimalType, Map<Integer, Boolean>> feed;
    private final Map<AnimalType, Map<Integer, Boolean>> product;
    private final Map<AnimalType, Map<Integer, Boolean>> animalUnlocked;
    private final Map<AnimalType, Boolean> unlocked;
    private final Map<AnimalType, Map<Integer, Instant>> instant;

    public static Animals createAnimals() {
        Map<AnimalType, Map<Integer, Boolean>> feed = new HashMap<>();
        Map<AnimalType, Map<Integer, Boolean>> product = new HashMap<>();
        Map<AnimalType, Map<Integer, Boolean>> animalUnlocked = new HashMap<>();
        Map<AnimalType, Boolean> unlocked = new HashMap<>();
        Map<AnimalType, Map<Integer, Instant>> instant = new HashMap<>();
        for (AnimalType value : AnimalType.values()) {
            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    feed.put(value, new HashMap<>());
                    product.put(value, new HashMap<>());
                    animalUnlocked.put(value, new HashMap<>());
                    instant.put(value, new HashMap<>());
                }
                feed.get(value).put(i, false);
                product.get(value).put(i, false);
                animalUnlocked.get(value).put(i, false);
                instant.get(value).put(i, Instant.EPOCH);
            }
            unlocked.put(value, false);
        }
        return new Animals(feed, product, animalUnlocked, unlocked, instant);
    }

    public void setFeed(AnimalType type, int animal, boolean value) {
        feed.get(type).put(animal, value);
    }

    public boolean getFeed(AnimalType type, int animal) {
        return feed.get(type).get(animal);
    }

    public void setProduct(AnimalType type, int animal, boolean value) {
        product.get(type).put(animal, value);
    }

    public boolean getProduct(AnimalType type, int animal) {
        return product.get(type).get(animal);
    }

    public void setAnimalUnlocked(AnimalType type, int animal, boolean value) {
        animalUnlocked.get(type).put(animal, value);
    }

    public boolean isAnimalUnlocked(AnimalType type, int animal) {
        return animalUnlocked.get(type).get(animal);
    }

    public void setUnlocked(AnimalType type, boolean isUnlocked) {
        unlocked.put(type, isUnlocked);
    }

    public boolean isUnlocked(AnimalType type) {
        return unlocked.get(type);
    }

    public void setInstant(AnimalType type, int animal, Instant value) {
        instant.get(type).put(animal, value);
    }

    public Instant getInstant(AnimalType type, int animal) {
        return instant.get(type).get(animal);
    }
}