package me.webhead1104.township.menus;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.Trains;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrainMenu extends View {

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title("Trains");
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        if (!Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getTrains().isUnlocked()) {
            context.setCancelled(true);
            return;
        }
        context.getPlayer().getInventory().clear();
        context.getPlayer().setItemOnCursor(ItemStack.empty());
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        Player player = context.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Trains trains = user.getTrains();
        int trainSlot = 0;
        for (int i = 1; i < 4; i++) {
            int finalI = i;
            context.slot(trainSlot).updateOnClick().onRender(slotRenderContext -> {
                Trains.Train train = trains.getTrain(finalI);
                ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
                @SuppressWarnings("SpellCheckingInspection")
                String textures = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1YjBlM2FhNjExNjJhM2M2NTM4OTg1YzVjMTFjZWI5NmQ1NjA1YjNjZTkyMzRjODhmNGZiZDcyMzQ3NWQifX19";
                itemStack.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile().addProperty(new ProfileProperty("textures", textures)));
                if (!train.isUnlocked()) {
                    itemStack = itemStack.withType(Material.COARSE_DIRT);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<red>Not unlocked"));
                    slotRenderContext.setItem(itemStack);
                    return;
                }
                if (!train.isInStation()) {
                    itemStack = itemStack.withType(Material.YELLOW_CONCRETE);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<white>Train: %d", finalI));
                    itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<white>Gone shopping"), Msg.format("<white>Be back in a bit"))));
                    slotRenderContext.setItem(itemStack);
                    return;
                }
                if (train.isClaimItems()) {
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click the train cars to claim the items!"));
                } else {
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Click the train cars to give items to the train!"));
                }
                slotRenderContext.setItem(itemStack);
            });

            int carSlot = trainSlot + 1;
            for (int j = 1; j < 6; j++) {
                int finalJ = j;
                context.slot(carSlot++).updateOnClick().onRender(slotRenderContext -> {
                    Trains.Train train = trains.getTrain(finalI);
                    if (!train.isUnlocked() || !train.isInStation()) return;
                    Trains.Train.TrainCar car = train.getTrainCar(finalJ);
                    if (train.isClaimItems()) {
                        if (car.getClaimItemType().equals(ItemType.NONE)) {
                            ItemStack itemStack = ItemStack.of(Material.IRON_INGOT);
                            itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Item claimed!"));
                            slotRenderContext.setItem(itemStack);
                            return;
                        }
                        ItemStack itemStack = ItemStack.of(Material.CHEST);
                        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Utils.thing2(car.getClaimItemType().name())));
                        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<white>%d", car.getClaimItemAmount()))));
                        slotRenderContext.setItem(itemStack);
                        return;
                    }
                    if (car.getGiveItemType().equals(ItemType.NONE)) {
                        ItemStack itemStack = ItemStack.of(Material.IRON_INGOT);
                        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<green>Items gave!"));
                        slotRenderContext.setItem(itemStack);
                        return;
                    }

                    ItemStack itemStack = ItemStack.of(Material.HOPPER);
                    itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Utils.thing2(car.getGiveItemType().name())));
                    if (user.getBarn().getItem(car.getGiveItemType()) >= car.getGiveItemAmount()) {
                        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>%d/%d", user.getBarn().getItem(car.getGiveItemType()), car.getGiveItemAmount()))));
                    } else {
                        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<red>%d/%d", user.getBarn().getItem(car.getGiveItemType()), car.getGiveItemAmount()))));
                    }
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    Trains.Train train = trains.getTrain(finalI);
                    Trains.Train.TrainCar trainCar = trains.getTrain(finalI).getTrainCar(finalJ);
                    if (train.isClaimItems()) {
                        user.getBarn().addAmountToItem(trainCar.getClaimItemType(), trainCar.getClaimItemAmount());
                        trainCar.setClaimItemType(ItemType.NONE);
                        trainCar.setClaimItemAmount(0);
                        AtomicBoolean good = new AtomicBoolean(true);
                        train.getTrainCars().forEach((key, value) -> {
                            if (!value.getClaimItemType().equals(ItemType.NONE) && value.getClaimItemAmount() != 0)
                                good.set(false);
                        });
                        if (good.get()) {
                            train.setClaimItems(false);
                            Trains.Train newTrain = new Trains.Train(finalI);
                            newTrain.setUnlocked(true);
                            user.getTrains().setTrain(finalI, newTrain);
                        }
                    } else {
                        if (trainCar.getGiveItemType().equals(ItemType.NONE)) return;
                        ItemType itemType = trainCar.getGiveItemType();
                        int amount = trainCar.getGiveItemAmount();
                        if (user.getBarn().getItem(itemType) >= amount) {
                            user.getBarn().removeAmountFromItem(itemType, amount);
                            trainCar.setGiveItemAmount(0);
                            trainCar.setGiveItemType(ItemType.NONE);
                            AtomicBoolean good = new AtomicBoolean(true);
                            train.getTrainCars().forEach((key, value) -> {
                                if (!value.getGiveItemType().equals(ItemType.NONE) && value.getGiveItemAmount() != 0)
                                    good.set(false);
                            });
                            if (good.get()) {
                                Trains.Train newTrain = new Trains.Train(finalI);
                                newTrain.setUnlocked(true);
                                newTrain.setClaimItems(true);
                                trains.setTrain(finalI, newTrain);
                            }
                            slotClickContext.update();
                        }
                    }
                });
            }
            trainSlot = trainSlot + 18;
        }
    }
}
