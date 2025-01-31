package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.ItemType;
import org.apache.commons.lang3.Validate;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class Trains {
    private final Map<Integer, Train> trains;
    private boolean unlocked = false;

    public Trains() {
        this.trains = new HashMap<>();
        for (int i = 1; i < 4; i++) this.trains.put(i, new Train(i));
    }

    public Train getTrain(int train) {
        Validate.inclusiveBetween(1, 3, train);
        return trains.get(train);
    }

    public void setTrain(Train train, int trains) {
        Validate.inclusiveBetween(1, 3, trains);
        this.trains.put(trains, train);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Train {
        private final Map<Integer, TrainCar> trainCars;
        private boolean unlocked = false;
        private boolean inStation = true;
        private boolean claimItems = false;
        private int coinsNeededToUnlock = 0;
        private int levelNeededToUnlock = 0;

        public Train(int train) {
            Validate.inclusiveBetween(1, 3, train);
            this.trainCars = new HashMap<>();
            for (int i = 1; i < 6; i++) {
                trainCars.put(i, new TrainCar());
            }
            if (train == 1) {
                this.unlocked = true;
            } else if (train == 2) {
                this.coinsNeededToUnlock = 1000;
                this.levelNeededToUnlock = 14;
            } else if (train == 3) {
                this.coinsNeededToUnlock = 14000;
                this.levelNeededToUnlock = 25;
            }
        }

        public TrainCar getTrainCar(int car) {
            Validate.inclusiveBetween(1, 5, car);
            return trainCars.get(car);
        }

        public void setTrainCar(int car, TrainCar trainCar) {
            Validate.inclusiveBetween(1, 5, car);
            trainCars.put(car, trainCar);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TrainCar {
        private Tuple<ItemType, Integer> claimItems;
        private Tuple<ItemType, Integer> giveItems;

        public TrainCar() {
            Random random = new Random();
            List<ItemType> giveList = new ArrayList<>(Arrays.stream(ItemType.values()).toList());
            List<ItemType> collectList = new ArrayList<>(List.of(ItemType.NAIL, ItemType.HAMMER, ItemType.PAINT));
            giveList.remove(ItemType.NONE);
            giveList.removeAll(List.of(ItemType.NAIL, ItemType.HAMMER, ItemType.PAINT));
            this.claimItems = new Tuple<>(collectList.get(random.nextInt(0, collectList.size())), random.nextInt(1, 32));
            this.giveItems = new Tuple<>(giveList.get(random.nextInt(0, giveList.size())), random.nextInt(1, 16));
        }
    }
}
