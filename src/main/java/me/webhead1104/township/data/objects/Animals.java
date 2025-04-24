package me.webhead1104.township.data.objects;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.AnimalType;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Animals {
    private final Map<AnimalType, AnimalBuilding> animals = new HashMap<>();

    public Animals() {
        for (AnimalType animalType : AnimalType.values()) {
            Map<Integer, AnimalBuilding.Animal> animals = new HashMap<>();
            for (int i = 0; i < 6; i++) {
                animals.put(i, new AnimalBuilding.Animal(false, false, i < 3, Instant.EPOCH));
            }
            this.animals.put(animalType, new AnimalBuilding(animals, false));
        }
    }

    public void setAnimalBuilding(@NotNull AnimalType animalType, @NotNull AnimalBuilding animal) {
        Preconditions.checkNotNull(animalType);
        Preconditions.checkNotNull(animal);
        animals.replace(animalType, animal);
    }

    public AnimalBuilding getAnimalBuilding(@NotNull AnimalType animalType) {
        Preconditions.checkNotNull(animalType);
        return animals.get(animalType);
    }

    @Getter
    @Setter
    public static class AnimalBuilding {
        private final Map<Integer, Animal> animals;
        private boolean unlocked;

        public AnimalBuilding(Map<Integer, Animal> animals, boolean unlocked) {
            this.animals = animals;
            this.unlocked = unlocked;
        }

        public void setAnimal(int slot, @NotNull Animal animal) {
            animals.put(slot, Preconditions.checkNotNull(animal));
        }

        public Animal getAnimal(int slot) {
            return animals.get(slot);
        }

        @Getter
        @Setter
        public static class Animal {
            private boolean feed;
            private boolean product;
            private boolean unlocked;
            private Instant instant;

            public Animal(boolean feed, boolean product, boolean unlocked, Instant instant) {
                this.feed = feed;
                this.product = product;
                this.unlocked = unlocked;
                this.instant = instant;
            }
        }
    }
}