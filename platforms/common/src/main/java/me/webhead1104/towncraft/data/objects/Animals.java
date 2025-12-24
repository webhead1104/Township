/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.data.objects;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.features.animals.AnimalType;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@ConfigSerializable
public class Animals {
    private final Map<Key, AnimalBuilding> animalBuildings = new HashMap<>();

    public Animals() {
        for (AnimalType.Animal animal : Towncraft.getDataLoader(AnimalType.class).values()) {
            Map<Integer, AnimalBuilding.Animal> animalBuildings = new HashMap<>();
            for (int i = 0; i < 6; i++) {
                animalBuildings.put(i, new AnimalBuilding.Animal(false, false, i < 3, Instant.EPOCH));
            }
            this.animalBuildings.put(animal.key(), new AnimalBuilding(animalBuildings));
        }
    }

    public AnimalBuilding getAnimalBuilding(@NotNull Key animalType) {
        Preconditions.checkNotNull(animalType);
        if (animalBuildings.containsKey(animalType)) {
            return animalBuildings.get(animalType);
        }
        AnimalBuilding animalBuilding = new AnimalBuilding();
        Map<Integer, AnimalBuilding.Animal> animalBuildings = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            animalBuildings.put(i, new AnimalBuilding.Animal(false, false, i < 3, Instant.EPOCH));
        }
        animalBuilding.setAnimals(animalBuildings);

        this.animalBuildings.put(animalType, animalBuilding);
        return animalBuilding;
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