package me.webhead1104.township.data.objects;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.data.enums.AnimalType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigSerializable
public class Animals {
    private final Map<AnimalType, AnimalBuilding> animalBuildings = new HashMap<>();

    public Animals() {
        for (AnimalType animalType : AnimalType.values()) {
            Map<Integer, AnimalBuilding.Animal> animalBuildings = new HashMap<>();
            for (int i = 0; i < 6; i++) {
                animalBuildings.put(i, new AnimalBuilding.Animal(false, false, i < 3, Instant.EPOCH));
            }
            this.animalBuildings.put(animalType, new AnimalBuilding(animalBuildings));
        }
    }

    public void setAnimalBuilding(@NotNull AnimalType animalType, @NotNull AnimalBuilding animal) {
        Preconditions.checkNotNull(animalType);
        Preconditions.checkNotNull(animal);
        animalBuildings.replace(animalType, animal);
    }

    public AnimalBuilding getAnimalBuilding(@NotNull AnimalType animalType) {
        Preconditions.checkNotNull(animalType);
        return animalBuildings.get(animalType);
    }

    @Getter
    @Setter
    @ConfigSerializable
    @NoArgsConstructor
    public static class AnimalBuilding {
        private Map<Integer, Animal> animals;

        public AnimalBuilding(Map<Integer, Animal> animals) {
            this.animals = animals;
        }

        public void setAnimal(int slot, @NotNull Animal animal) {
            animals.put(slot, Preconditions.checkNotNull(animal));
        }

        public Animal getAnimal(int slot) {
            return animals.get(slot);
        }

        @Getter
        @Setter
        @ConfigSerializable
        @NoArgsConstructor
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