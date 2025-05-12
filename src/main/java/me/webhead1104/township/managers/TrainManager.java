package me.webhead1104.township.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.Trains;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.*;
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
        if (!user.getTrains().isUnlocked()) return;
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
                            Trains.Train.TrainCar car = train.getTrainCar(j);
                            if (car.getClaimItem().getItemType().equals(ItemType.NONE)) {
                                carItem.material(Material.IRON_INGOT);
                                carItem.displayName(Msg.format("<green>Item claimed!"));
                            } else {
                                carItem.material(Material.CHEST);
                                carItem.pdcSetInt(Keys.trainKey, i);
                                carItem.pdcSetInt(Keys.itemAmountKey, car.getClaimItem().getAmount());
                                carItem.pdcSetString(Keys.typeKey, car.getClaimItem().getItemType().name());
                                carItem.pdcSetInt(Keys.trainCarKey, j);
                                carItem.id("train_collect");
                                carItem.displayName(car.getClaimItem().getItemType().getItemStack().getItemMeta().displayName());
                                carItem.lore(Msg.format("<white>" + car.getClaimItem().getAmount()));
                            }
                            inventory.setItem(carSlot.getAndAdd(1), carItem.build());
                        }
                    } else {
                        engine.displayName(Msg.format("<green>Click the train cars to give items to the train!"));
                        AtomicInteger carSlot = new AtomicInteger(trainSlot.get() + 1);
                        for (int j = 1; j < 6; j++) {
                            ItemBuilder carItem = new ItemBuilder(MenuItems.trainCar);
                            Trains.Train.TrainCar car = train.getTrainCar(j);
                            if (car.getGiveItem().getItemType().equals(ItemType.NONE)) {
                                carItem.material(Material.IRON_INGOT);
                                carItem.displayName(Msg.format("<green>Items gave!"));
                            } else {
                                carItem.material(Material.HOPPER);
                                carItem.displayName(car.getGiveItem().getItemType().getItemStack().getItemMeta().displayName());
                                if (user.getBarn().getItem(car.getGiveItem().getItemType()) >= car.getGiveItem().getAmount()) {
                                    carItem.lore(List.of(Msg.format("<green>You need " + car.getGiveItem().getAmount()),
                                            Msg.format("<green>You have " + (user.getBarn().getItem(car.getGiveItem().getItemType()) == 0 ? "none" : user.getBarn().getItem(car.getClaimItem().getItemType())))));
                                } else {
                                    carItem.lore(List.of(Msg.format("<red>You need " + car.getGiveItem().getItemType()),
                                            Msg.format("<red>You have " + (user.getBarn().getItem(car.getGiveItem().getItemType()) == 0 ? "none" : user.getBarn().getItem(car.getClaimItem().getItemType())))));
                                }
                                carItem.id("train_give");
                                carItem.pdcSetInt(Keys.trainKey, i);
                                carItem.pdcSetInt(Keys.itemAmountKey, car.getGiveItem().getAmount());
                                carItem.pdcSetString(Keys.typeKey, car.getGiveItem().getItemType().name());
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
                    level = Msg.format("<green>You need to be level " + train.getLevelNeededToUnlock() + " to purchase this train! <white>" + user.getLevel().getLevel() + "<aqua>/<green>" + train.getLevelNeededToUnlock());
                } else {
                    level = Msg.format("<red>You need to be level " + train.getLevelNeededToUnlock() + " to purchase this train! <white>You are level " + user.getLevel().getLevel());
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
        Utils.openInventory(player, inventory, uuid -> Township.getWorldManager().openWorldMenu(player), null);
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
        user.getTrains().getTrain(train).getTrainCar(car).setClaimItem(new Trains.Train.TrainCar.TrainCarItem(ItemType.NONE, 0));
        Trains.Train trains = user.getTrains().getTrain(train);
        AtomicBoolean good = new AtomicBoolean(true);
        trains.getTrainCars().forEach((key, value) -> {
            if (!value.getClaimItem().getItemType().equals(ItemType.NONE) && value.getGiveItem().getAmount() != 0)
                good.set(false);
        });
        if (good.get()) {
            trains.setClaimItems(false);
            Trains.Train newTrain = new Trains.Train(train);
            newTrain.setUnlocked(true);
            user.getTrains().setTrain(newTrain, train);
        }
        openMenu(player);
    }

    public void giveItem(Player player, ItemType itemType, int amount, int train, int car) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getBarn().getItem(itemType) >= amount) {
            user.getBarn().removeAmountFromItem(itemType, amount);
            Trains.Train.TrainCar.TrainCarItem item = new Trains.Train.TrainCar.TrainCarItem(ItemType.NONE, 0);
            user.getTrains().getTrain(train).getTrainCar(car).setGiveItem(item);
            Trains.Train trains = user.getTrains().getTrain(train);
            AtomicBoolean good = new AtomicBoolean(true);
            trains.getTrainCars().forEach((key, value) -> {
                if (!value.getGiveItem().getItemType().equals(ItemType.NONE) && value.getGiveItem().getAmount() != 0)
                    good.set(false);
            });
            if (good.get()) {
                user.getTrains().setTrain(new Trains.Train(train), train);
                user.getTrains().getTrain(train).setUnlocked(true);
                user.getTrains().getTrain(train).setClaimItems(true);
            }
            openMenu(player);
        }
    }
}
