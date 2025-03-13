package me.webhead1104.township.data.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.datafixer.TownshipCodecs;
import me.webhead1104.township.data.enums.ItemType;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Setter
public class Trains {
    public static final @NotNull Codec<Trains> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(TownshipCodecs.INT, Train.CODEC).fieldOf("trains").forGetter(Trains::getTrains),
            Codec.BOOL.fieldOf("unlocked").forGetter(Trains::isUnlocked)
    ).apply(instance, Trains::new));
    private final Map<Integer, Train> trains = new HashMap<>();
    private boolean unlocked = false;

    public Trains(Map<Integer, Train> trains, boolean unlocked) {
        this.trains.putAll(trains);
        this.unlocked = unlocked;
    }

    public Trains() {
        for (int i = 1; i < 4; i++) {
            this.trains.put(i, new Train(i));
        }
    }

    public Train getTrain(int train) {
        return trains.get(train);
    }

    public void setTrain(Train train, int trains) {
        this.trains.put(trains, train);
    }

    @Getter
    @Setter
    public static class Train {
        public static final @NotNull Codec<Train> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(TownshipCodecs.INT, TrainCar.CODEC).fieldOf("trainCars").forGetter(Train::getTrainCars),
                Codec.BOOL.fieldOf("unlocked").forGetter(Train::isUnlocked),
                Codec.BOOL.fieldOf("inStation").forGetter(Train::isInStation),
                Codec.BOOL.fieldOf("claimItems").forGetter(Train::isClaimItems),
                Codec.INT.fieldOf("coinsNeededToUnlock").forGetter(Train::getCoinsNeededToUnlock),
                Codec.INT.fieldOf("levelNeededToUnlock").forGetter(Train::getLevelNeededToUnlock)
        ).apply(instance, Train::new));
        private final Map<Integer, TrainCar> trainCars = new HashMap<>();
        private boolean unlocked = false;
        private boolean inStation = true;
        private boolean claimItems = false;
        private int coinsNeededToUnlock = 0;
        private int levelNeededToUnlock = 0;

        public Train(Map<Integer, TrainCar> trainCars, boolean unlocked, boolean inStation, boolean claimItems, int coinsNeededToUnlock, int levelNeededToUnlock) {
            this.trainCars.putAll(trainCars);
            this.unlocked = unlocked;
            this.inStation = inStation;
            this.claimItems = claimItems;
            this.coinsNeededToUnlock = coinsNeededToUnlock;
            this.levelNeededToUnlock = levelNeededToUnlock;
        }

        public Train(int train) {
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

        @Getter
        @Setter
        public static class TrainCar {
            public static final @NotNull Codec<TrainCar> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    TrainCarItem.CODEC.fieldOf("claimItem").forGetter(TrainCar::getClaimItem),
                    TrainCarItem.CODEC.fieldOf("giveItem").forGetter(TrainCar::getGiveItem)
            ).apply(instance, TrainCar::new));
            private TrainCarItem claimItem;
            private TrainCarItem giveItem;

            public TrainCar(TrainCarItem claimItem, TrainCarItem giveItem) {
                this.claimItem = claimItem;
                this.giveItem = giveItem;
            }

            public TrainCar() {
                Random random = new Random();
                List<ItemType> giveList = new ArrayList<>(Arrays.stream(ItemType.values()).toList());
                List<ItemType> collectList = new ArrayList<>(List.of(ItemType.NAIL, ItemType.HAMMER, ItemType.PAINT));
                giveList.remove(ItemType.NONE);
                giveList.removeAll(List.of(ItemType.NAIL, ItemType.HAMMER, ItemType.PAINT));
                this.claimItem = new TrainCarItem(collectList.get(random.nextInt(0, collectList.size())), random.nextInt(1, 32));
                this.giveItem = new TrainCarItem(giveList.get(random.nextInt(0, giveList.size())), random.nextInt(1, 16));
            }

            @Getter
            @Setter
            public static class TrainCarItem {
                public static final @NotNull Codec<TrainCarItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        ItemType.CODEC.fieldOf("itemType").forGetter(TrainCarItem::getItemType),
                        Codec.INT.fieldOf("amount").forGetter(TrainCarItem::getAmount)
                ).apply(instance, TrainCarItem::new));
                private ItemType itemType;
                private int amount;

                public TrainCarItem(ItemType itemType, int amount) {
                    this.itemType = itemType;
                    this.amount = amount;
                }
            }
        }
    }
}
