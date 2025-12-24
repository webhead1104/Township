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

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import net.kyori.adventure.key.Key;
import org.apache.commons.lang3.Validate;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@ConfigSerializable
public class Trains {
    private final Map<Integer, Train> trains = new HashMap<>();
    private boolean unlocked = false;

    public Trains() {
        for (int i = 1; i < 4; i++) {
            this.trains.put(i, new Train(i));
        }
    }

    public Train getTrain(int train) {
        return trains.get(train);
    }

    public void setTrain(int trainSlot, Train train) {
        trains.put(trainSlot, train);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @ConfigSerializable
    @NoArgsConstructor
    public static class Train {
        private final Map<Integer, TrainCar> trainCars = new HashMap<>();
        private boolean unlocked = false;
        private boolean inStation = true;
        private boolean claimItems = false;

        public Train(int train) {
            for (int i = 1; i < 6; i++) {
                trainCars.put(i, new TrainCar());
            }
            if (train == 1) {
                this.unlocked = true;
            }
        }

        public TrainCar getTrainCar(int car) {
            Validate.inclusiveBetween(1, 5, car);
            return trainCars.get(car);
        }

        @Getter
        @Setter
        @AllArgsConstructor
        @ConfigSerializable
        public static class TrainCar {
            private Key claimItemType;
            private int claimItemAmount;
            private Key giveItemType;
            private int giveItemAmount;

            public TrainCar() {
                SplittableRandom random = new SplittableRandom();
                List<Key> giveList = new ArrayList<>(Towncraft.getDataLoader(ItemType.class).keys());
                giveList.remove(Towncraft.NONE_KEY);
                giveList.removeAll(List.of(Towncraft.key("nail"), Towncraft.key("hammer"), Towncraft.key("paint")));

                List<Key> collectList = new ArrayList<>(List.of(Towncraft.key("nail"), Towncraft.key("hammer"), Towncraft.key("paint")));

                this.claimItemType = collectList.get(random.nextInt(0, collectList.size()));
                this.claimItemAmount = random.nextInt(1, 32);

                this.giveItemType = giveList.get(random.nextInt(0, giveList.size()));
                this.giveItemAmount = random.nextInt(1, 16);
            }
        }
    }
}
