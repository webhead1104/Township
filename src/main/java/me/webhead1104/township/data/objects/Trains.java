package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.ItemType;
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
                List<Key> giveList = new ArrayList<>(ItemType.keys());
                giveList.remove(Township.noneKey);
                giveList.removeAll(List.of(Township.key("nail"), Township.key("hammer"), Township.key("paint")));

                List<Key> collectList = new ArrayList<>(List.of(Township.key("nail"), Township.key("hammer"), Township.key("paint")));

                this.claimItemType = collectList.get(random.nextInt(0, collectList.size()));
                this.claimItemAmount = random.nextInt(1, 32);

                this.giveItemType = giveList.get(random.nextInt(0, giveList.size()));
                this.giveItemAmount = random.nextInt(1, 16);
            }
        }
    }
}
