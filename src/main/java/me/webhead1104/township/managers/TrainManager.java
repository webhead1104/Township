package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.Trains;
import me.webhead1104.township.data.objects.Tuple;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.Keys;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
public class TrainManager {

    public void openMenu(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (!(user.getLevel().getLevel() >= 5)) return;
        player.setItemOnCursor(ItemStack.empty());
        player.getInventory().clear();
        Inventory inventory = Bukkit.createInventory(null, 54, Msg.format("The Trains"));
        Trains trains = user.getTrains();
        AtomicInteger trainSlot = new AtomicInteger(0);
        for (int i = 1; i < 4; i++) {
            ItemBuilder engine = new ItemBuilder(MenuItems.trainEngine);
            Trains.Train train = trains.getTrain(i);
            if (train.isUnlocked()) {
                if (train.isInStation()) {
                    if (train.isClaimItems()) {
                        engine.displayName(Msg.format("<green>Click the train cars to claim the items!"));
                        AtomicInteger carSlot = new AtomicInteger(trainSlot.get() + 1);
                        for (int j = 1; j < 6; j++) {
                            ItemBuilder carItem = new ItemBuilder(MenuItems.trainCar);
                            Trains.TrainCar car = train.getTrainCar(j);
                            if (car.getClaimItems().getA().equals(ItemType.NONE)) {
                                carItem.material(Material.IRON_INGOT);
                                carItem.displayName(Msg.format("<green>Item claimed!"));
                            } else {
                                carItem.material(Material.CHEST);
                                carItem.pdcSetInt(Keys.trainKey, i);
                                carItem.pdcSetInt(Keys.itemAmountKey, car.getClaimItems().getB());
                                carItem.pdcSetString(Keys.itemTypeKey, car.getClaimItems().getA().name());
                                carItem.pdcSetInt(Keys.trainCarKey, j);
                                carItem.id("train_collect");
                                carItem.displayName(car.getClaimItems().getA().getItemStack().getItemMeta().displayName());
                                carItem.lore(Msg.format("<white>" + car.getClaimItems().getB()));
                            }
                            inventory.setItem(carSlot.getAndAdd(1), carItem.build());
                        }
                    } else {
                        engine.displayName(Msg.format("<green>Click the train cars to give items to the train!"));
                        AtomicInteger carSlot = new AtomicInteger(trainSlot.get() + 1);
                        for (int j = 1; j < 6; j++) {
                            ItemBuilder carItem = new ItemBuilder(MenuItems.trainCar);
                            Trains.TrainCar car = train.getTrainCar(j);
                            if (car.getGiveItems().getA().equals(ItemType.NONE)) {
                                carItem.material(Material.IRON_INGOT);
                                carItem.displayName(Msg.format("<green>Items gave!"));
                            } else {
                                carItem.material(Material.HOPPER);
                                carItem.displayName(car.getGiveItems().getA().getItemStack().getItemMeta().displayName());
                                if (user.getBarn().getItem(car.getGiveItems().getA()) >= car.getGiveItems().getB()) {
                                    carItem.lore(List.of(Msg.format("<green>You need " + car.getGiveItems().getB()), Msg.format("<green>You have " + (user.getBarn().getItem(car.getGiveItems().getA()) == 0 ? "none" : user.getBarn().getItem(car.getGiveItems().getA())))));
                                } else {
                                    carItem.lore(List.of(Msg.format("<red>You need " + car.getGiveItems().getB()), Msg.format("<red>You have " + (user.getBarn().getItem(car.getGiveItems().getA()) == 0 ? "none" : user.getBarn().getItem(car.getGiveItems().getA())))));
                                }
                                carItem.id("train_give");
                                carItem.pdcSetInt(Keys.trainKey, i);
                                carItem.pdcSetInt(Keys.itemAmountKey, car.getGiveItems().getB());
                                carItem.pdcSetString(Keys.itemTypeKey, car.getGiveItems().getA().name());
                                carItem.pdcSetInt(Keys.trainCarKey, j);
                            }
                            inventory.setItem(carSlot.getAndAdd(1), carItem.build());
                        }
                    }
                    engine.material(Material.IRON_BLOCK);
                } else {
                    engine.displayName(Msg.format("<white>Train " + i));
                    engine.lore(List.of(Msg.format("<white>Gone shopping"), Msg.format("<white>Be back in a bit")));
                    engine.material(Material.YELLOW_CONCRETE);
                }
            } else {
                engine.displayName(Msg.format("<red>Not unlocked"));
                Component coins;
                if (user.getCoins() >= train.getCoinsNeededToUnlock()) {
                    coins = Msg.format("<green>You need " + train.getCoinsNeededToUnlock() + " coins to purchase this train! <white>" + user.getCoins() + "<aqua>/<green>" + train.getCoinsNeededToUnlock());
                } else {
                    coins = Msg.format("<red>You need " + train.getCoinsNeededToUnlock() + " coins to purchase this train! <white>" + user.getCoins() + "<aqua>/<red>" + train.getCoinsNeededToUnlock());
                }
                Component level;
                if (user.getLevel().getLevel() >= train.getLevelNeededToUnlock()) {
                    level = Msg.format("<green>You need to be level " + train.getLevelNeededToUnlock() + " to purchase this train! <white>" + user.getLevel() + "<aqua>/<green>" + train.getLevelNeededToUnlock());
                } else {
                    level = Msg.format("<red>You need to be level " + train.getLevelNeededToUnlock() + " to purchase this train! <white>You are level " + user.getLevel());
                }
                if (user.getCoins() >= train.getCoinsNeededToUnlock() && user.getLevel().getLevel() >= train.getLevelNeededToUnlock()) {
                    engine.lore(List.of(Msg.format("<green>You can purchase this!"), coins, level));
                } else engine.lore(List.of(coins, level));
                engine.material(Material.COARSE_DIRT);
                engine.id("train_buy");
            }
            engine.pdcSetInt(Keys.trainKey, i);
            inventory.setItem(trainSlot.getAndAdd(18), engine.build());
        }
        inventory.setItem(53, MenuItems.backButton);
        player.openInventory(inventory);
    }

    public void purchaseTrain(Player player, int train) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Trains trains = user.getTrains();
        int coins = trains.getTrain(train).getCoinsNeededToUnlock();
        int level = trains.getTrain(train).getLevelNeededToUnlock();
        if (user.getCoins() >= coins && user.getLevel().getLevel() >= level) {
            user.setCoins(user.getCoins() - coins);
            trains.getTrain(train).setUnlocked(true);
            openMenu(player);
        }
    }

    public void collectItem(Player player, ItemType itemType, int amount, int train, int car) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getBarn().addAmountToItem(itemType, amount);
        user.getTrains().getTrain(train).getTrainCar(car).setClaimItems(new Tuple<>(ItemType.NONE, 0));
        Trains.Train trains = user.getTrains().getTrain(train);
        AtomicBoolean good = new AtomicBoolean(true);
        trains.getTrainCars().forEach((key, value) -> {
            if (!value.getClaimItems().getA().equals(ItemType.NONE) && value.getGiveItems().getB() != 0)
                good.set(false);
        });
        if (good.get()) {
            trains.setClaimItems(false);
            Trains.Train newTrain = Trains.Train.createTrain(train);
            newTrain.setUnlocked(true);
            user.getTrains().setTrain(newTrain, train);
        }
        openMenu(player);
    }

    public void giveItem(Player player, ItemType itemType, int amount, int train, int car) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getBarn().getItem(itemType) >= amount) {
            user.getBarn().removeAmountFromItem(itemType, amount);
            Tuple<ItemType, Integer> tuple = new Tuple<>(ItemType.NONE, 0);
            user.getTrains().getTrain(train).getTrainCar(car).setGiveItems(tuple);
            Trains.Train trains = user.getTrains().getTrain(train);
            AtomicBoolean good = new AtomicBoolean(true);
            trains.getTrainCars().forEach((key, value) -> {
                if (!value.getGiveItems().getA().equals(ItemType.NONE) && value.getGiveItems().getB() != 0)
                    good.set(false);
            });
            if (good.get()) {
                user.getTrains().setTrain(Trains.Train.createTrain(train), train);
                user.getTrains().getTrain(train).setUnlocked(true);
                user.getTrains().getTrain(train).setClaimItems(true);
            }
            openMenu(player);
        }
    }
}
