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
package me.webhead1104.towncraft.features.trains;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.Trains;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrainMenu extends TowncraftView {

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title("Trains");
    }

    @Override
    public void onOpen(@NotNull OpenContext context) {
        if (!userState.get(context).getTrains().isUnlocked()) {
            context.setCancelled(true);
            return;
        }
        super.onOpen(context);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        User user = userState.get(context);
        Trains trains = user.getTrains();
        int trainSlot = 0;
        for (int i = 1; i < 4; i++) {
            int finalI = i;
            context.slot(trainSlot).updateOnClick().onRender(slotRenderContext -> {
                Trains.Train train = trains.getTrain(finalI);
                TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
                String textures = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1YjBlM2FhNjExNjJhM2M2NTM4OTg1YzVjMTFjZWI5NmQ1NjA1YjNjZTkyMzRjODhmNGZiZDcyMzQ3NWQifX19";
                itemStack.setPlayerHeadTexture(textures);
                if (!train.isUnlocked()) {
                    itemStack.setMaterial(TowncraftMaterial.COARSE_DIRT);
                    itemStack.setName(Msg.format("<red>Not unlocked"));
                    slotRenderContext.setItem(itemStack);
                    return;
                }
                if (!train.isInStation()) {
                    itemStack.setMaterial(TowncraftMaterial.YELLOW_CONCRETE);
                    itemStack.setName(Msg.format("<white>Train: %d", finalI));
                    itemStack.setLore(List.of(Msg.format("<white>Gone shopping"), Msg.format("<white>Be back in a bit")));
                    slotRenderContext.setItem(itemStack);
                    return;
                }
                if (train.isClaimItems()) {
                    itemStack.setName(Msg.format("<green>Click the train cars to claim the items!"));
                } else {
                    itemStack.setName(Msg.format("<green>Click the train cars to give items to the train!"));
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
                        if (car.getClaimItemType().equals(Towncraft.NONE_KEY)) {
                            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.IRON_INGOT);
                            itemStack.setName(Msg.format("<green>Item claimed!"));
                            slotRenderContext.setItem(itemStack);
                            return;
                        }
                        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.CHEST);
                        itemStack.setName(Msg.format(Utils.thing2(car.getClaimItemType().value())));
                        itemStack.setLore(Msg.format("<white>%d", car.getClaimItemAmount()));
                        slotRenderContext.setItem(itemStack);
                        return;
                    }
                    if (car.getGiveItemType().equals(Towncraft.NONE_KEY)) {
                        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.IRON_INGOT);
                        itemStack.setName(Msg.format("<green>Items gave!"));
                        slotRenderContext.setItem(itemStack);
                        return;
                    }

                    TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.HOPPER);
                    itemStack.setName(Msg.format(Utils.thing2(car.getGiveItemType().value())));
                    if (user.getBarn().getItem(car.getGiveItemType()) >= car.getGiveItemAmount()) {
                        itemStack.setLore(Msg.format("<green>%d/%d", user.getBarn().getItem(car.getGiveItemType()), car.getGiveItemAmount()));
                    } else {
                        itemStack.setLore(Msg.format("<red>%d/%d", user.getBarn().getItem(car.getGiveItemType()), car.getGiveItemAmount()));
                    }
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    Trains.Train train = trains.getTrain(finalI);
                    Trains.Train.TrainCar trainCar = trains.getTrain(finalI).getTrainCar(finalJ);
                    if (train.isClaimItems()) {
                        user.getBarn().addAmountToItem(trainCar.getClaimItemType(), trainCar.getClaimItemAmount());
                        trainCar.setClaimItemType(Towncraft.NONE_KEY);
                        trainCar.setClaimItemAmount(0);
                        AtomicBoolean good = new AtomicBoolean(true);
                        train.getTrainCars().forEach((key, value) -> {
                            if (!value.getClaimItemType().equals(Towncraft.NONE_KEY) && value.getClaimItemAmount() != 0)
                                good.set(false);
                        });
                        if (good.get()) {
                            train.setClaimItems(false);
                            Trains.Train newTrain = new Trains.Train(finalI);
                            newTrain.setUnlocked(true);
                            user.getTrains().setTrain(finalI, newTrain);
                        }
                    } else {
                        if (trainCar.getGiveItemType().equals(Towncraft.NONE_KEY)) return;
                        Key itemType = trainCar.getGiveItemType();
                        int amount = trainCar.getGiveItemAmount();
                        if (user.getBarn().getItem(itemType) >= amount) {
                            user.getBarn().removeAmountFromItem(itemType, amount);
                            user.addXp(4);
                            trainCar.setGiveItemAmount(0);
                            trainCar.setGiveItemType(Towncraft.NONE_KEY);
                            AtomicBoolean good = new AtomicBoolean(true);
                            train.getTrainCars().forEach((key, value) -> {
                                if (!value.getGiveItemType().equals(Towncraft.NONE_KEY) && value.getGiveItemAmount() != 0)
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
