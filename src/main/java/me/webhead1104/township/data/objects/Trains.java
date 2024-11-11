package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.enums.ItemType;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@AllArgsConstructor
public class Trains {
    private Map<Integer, Train> trains;

    public static Trains createTrains() {
        Map<Integer, Train> trains = new HashMap<>();
        for (int i = 1; i < 4; i++) trains.put(i, Train.createTrain(i));
        return new Trains(trains);
    }

    public Train getTrain(int train) {
        Validate.inclusiveBetween(1, 3, train);
        return trains.get(train);
    }

    public void setTrain(Train train, int trains) {
        Validate.inclusiveBetween(1, 3, trains);
        this.trains.put(trains, train);
    }

    public boolean isTrainUnlocked(int train) {
        Validate.inclusiveBetween(1, 3, train);
        if (trains.containsKey(train)) {
            return trains.get(train).isUnlocked();
        } else return false;
    }

    public boolean isInStation(int train) {
        Validate.inclusiveBetween(1, 3, train);
        return trains.get(train).isInStation();
    }

    public int getTrainsUnlocked() {
        AtomicInteger a = new AtomicInteger(0);
        for (int i = 1; i < 4; i++) {
            if (isTrainUnlocked(i)) a.getAndIncrement();
        }
        return a.get();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Train {
        private Map<Integer, TrainCar> trainCars;
        private boolean unlocked;
        private boolean inStation;
        private boolean claimItems;
        private int coinsNeededToUnlock;
        private int levelNeededToUnlock;

        public static Train createTrain(int train) {
            Validate.inclusiveBetween(1, 3, train);
            Map<Integer, TrainCar> trainCars = new HashMap<>();
            for (int i = 1; i < 6; i++) {
                trainCars.put(i, TrainCar.generateTrainCar());
            }
            int coinsNeededToUnlock = 0;
            int levelNeededToUnlock = 0;
            boolean unlocked = false;
            if (train == 1) {
                unlocked = true;
            } else if (train == 2) {
                coinsNeededToUnlock = 1000;
                levelNeededToUnlock = 14;
            } else if (train == 3) {
                coinsNeededToUnlock = 14000;
                levelNeededToUnlock = 25;
            }
            return new Train(trainCars, unlocked, true, false, coinsNeededToUnlock, levelNeededToUnlock);
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

        public static Trains.TrainCar generateTrainCar() {
            Random random = new Random();
            List<ItemType> giveList = new ArrayList<>(Arrays.stream(ItemType.values()).toList());
            List<ItemType> collectList = new ArrayList<>(List.of(ItemType.NAIL, ItemType.HAMMER, ItemType.PAINT));
            giveList.remove(ItemType.NONE);
            giveList.removeAll(List.of(ItemType.NAIL, ItemType.HAMMER, ItemType.PAINT));
            ItemType collectItem = collectList.get(random.nextInt(0, collectList.size()));
            int collectItemAmount = random.nextInt(1, 32);
            ItemType giveItem = giveList.get(random.nextInt(0, giveList.size()));
            int giveItemAmount = random.nextInt(1, 16);
            return new Trains.TrainCar(new Tuple<>(collectItem, collectItemAmount), new Tuple<>(giveItem, giveItemAmount));
        }
    }
}
