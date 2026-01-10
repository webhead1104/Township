/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
package me.webhead1104.towncraft.features.barn;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.MutableIntState;
import me.devnatan.inventoryframework.state.MutableState;
import me.devnatan.inventoryframework.state.State;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.Barn;
import me.webhead1104.towncraft.data.objects.BarnUpgrade;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.Context;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.utils.Msg;
import me.webhead1104.towncraft.utils.Utils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BarnMenu extends TowncraftView {
    private static final Key hammerKey = Towncraft.key("hammer");
    private static final Key nailKey = Towncraft.key("nail");
    private static final Key paintKey = Towncraft.key("paint");
    private final MutableIntState sellAmount = mutableState(1);
    private final MutableState<Key> sellItem = mutableState(Towncraft.NONE_KEY);
    private final State<Barn> barnState = computedState(context -> userState.get(context).getBarn());

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.scheduleUpdate(20);
        config.cancelInteractions();
        config.size(6);
        config.title("Barn");
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        setInventoryItems(context);

        for (Key item : barnState.get(context).getItemMap().keySet()) {
            if (item.equals(Towncraft.NONE_KEY)) continue;
            context.availableSlot().updateOnStateChange(sellAmount).hideIf(hideContext -> barnState.get(hideContext).getItem(item) <= 0)
                    .onRender(slotRenderContext -> {
                        TowncraftItemStack itemStack = Towncraft.getDataLoader(ItemType.class).get(item).getItemStack();
                        itemStack.setName(Msg.format("%s: <yellow>%d", Towncraft.getDataLoader(ItemType.class).get(item).getName(), barnState.get(slotRenderContext).getItem(item)));
                        itemStack.overrideNameColor();
                        slotRenderContext.setItem(itemStack);
                    }).onClick(slotClickContext -> {
                        sellItem.set(item, slotClickContext);
                        sellAmount.set(1, slotClickContext);
                        slotClickContext.update();
                    });
        }

        context.slot(48).hideIf(hideContext -> sellItem.get(hideContext).equals(Towncraft.NONE_KEY) || sellAmount.get(hideContext) == 1)
                .updateOnStateChange(sellAmount).onRender(slotRenderContext -> {
                    TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.RED_CANDLE);
                    itemStack.setName(Msg.format("<red>Click to decrease the amount!"));
                    itemStack.setLore(Msg.format("<blue>Currently at %d", sellAmount.get(slotRenderContext)));
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    sellAmount.decrement(slotClickContext);
                    slotClickContext.update();
                });

        context.slot(49).hideIf(hideContext -> sellItem.get(hideContext).equals(Towncraft.NONE_KEY))
                .updateOnStateChange(sellAmount).onRender(slotRenderContext -> {
                    ItemType.Item item = Towncraft.getDataLoader(ItemType.class).get(sellItem.get(slotRenderContext));
                    int amount = sellAmount.get(slotRenderContext);
                    TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
                    itemStack.setName(Msg.format("<green>Click to sell <aqua>%d <green>of <yellow>%s <green>for <aqua>%d <gold>coins!", amount, item.getName(), item.getSellPrice() * amount));
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    ItemType.Item item = Towncraft.getDataLoader(ItemType.class).get(sellItem.get(slotClickContext));
                    int amount = sellAmount.get(slotClickContext);
                    if (barnState.get(slotClickContext).getItem(item.key()) < amount) return;
                    barnState.get(slotClickContext).removeAmountFromItem(item.key(), amount);
                    User user = userState.get(slotClickContext);
                    user.setCoins(user.getCoins() + item.getSellPrice() * amount);
                    sellItem.set(Towncraft.NONE_KEY, slotClickContext);
                    sellAmount.set(1, slotClickContext);
                    slotClickContext.openForPlayer(BarnMenu.class);
                });

        context.slot(50).hideIf(hideContext -> sellItem.get(hideContext).equals(Towncraft.NONE_KEY) ||
                        barnState.get(hideContext).getItem(sellItem.get(hideContext)) <= sellAmount.get(hideContext))
                .updateOnStateChange(sellAmount).onRender(slotRenderContext -> {
                    TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.GREEN_CANDLE);
                    itemStack.setName(Msg.format("<green>Click to increase the amount!"));
                    itemStack.setLore(Msg.format("<blue>Currently at %d", sellAmount.get(slotRenderContext)));
                    slotRenderContext.setItem(itemStack);
                }).onClick(slotClickContext -> {
                    sellAmount.increment(slotClickContext);
                    slotClickContext.update();
                });
    }

    @Override
    public void onUpdate(@NotNull Context context) {
        setInventoryItems(context);
    }

    private boolean canUpgrade(User user) {
        int toolsNeeded = user.getBarn().getBarnUpgrade().getToolsNeeded();
        return user.getBarn().getItem(hammerKey) >= toolsNeeded && user.getBarn().getItem(nailKey) >= toolsNeeded && user.getBarn().getItem(paintKey) >= toolsNeeded;
    }

    @Override
    public void onClick(@NotNull SlotClickContext context) {
        if (!context.isOnEntityContainer()) return;
        if (context.getSlot() == 81) {
            context.closeForPlayer();
            return;
        }
        if (context.getSlot() == 89) {
            User user = userState.get(context);
            if (canUpgrade(user)) {
                BarnUpgrade newUpgrade = Towncraft.getDataLoader(BarnUpdateDataLoader.class).get(user.getBarn().getBarnUpgrade().getId());
                if (newUpgrade == null) {
                    throw new NullPointerException("barn upgrade not found");
                }
                user.getBarn().setBarnUpgrade(newUpgrade);
                user.getBarn().removeAmountFromItem(hammerKey, newUpgrade.getToolsNeeded());
                user.getBarn().removeAmountFromItem(nailKey, newUpgrade.getToolsNeeded());
                user.getBarn().removeAmountFromItem(paintKey, newUpgrade.getToolsNeeded());
                context.openForPlayer(BarnMenu.class);
            }
        }
    }

    private void setInventoryItems(Context context) {
        Barn barn = barnState.get(context);
        TowncraftPlayer player = context.getPlayer();
        TowncraftItemStack storage;
        if (barn.getBarnUpgrade().getBarnStorage() > barn.getStorage()) {
            storage = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
            storage.setName(Msg.format("<green>%d/%d", barn.getStorage(), barn.getBarnUpgrade().getBarnStorage()));
        } else {
            storage = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
            storage.setName(Msg.format("<red>Full! %d/%d", barn.getStorage(), barn.getBarnUpgrade().getBarnStorage()));
        }
        List<Component> storageLore = new ArrayList<>();
        barn.getItemMap().forEach((key, value) -> {
            if (value != 0) {
                storageLore.add(Msg.format("<white>%s: <yellow>%d", Towncraft.getDataLoader(ItemType.class).get(key).getName(), value));
            }
        });
        storage.setLore(storageLore);
        player.getInventory().setItem(1, storage);

        TowncraftItemStack upgrade;
        List<Component> upgradeLore = new ArrayList<>();
        if (canUpgrade(userState.get(context))) {
            upgrade = TowncraftItemStack.of(TowncraftMaterial.LIME_CONCRETE);
            upgradeLore.add(Msg.format("<green>Click to upgrade!"));
        } else {
            upgrade = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
            upgradeLore.add(Msg.format("<red>You need to get more materials to upgrade!"));
        }
        int toolsNeeded = barn.getBarnUpgrade().getToolsNeeded();
        upgradeLore.add(Utils.addResourceLine("Hammers", barn.getItem(hammerKey), toolsNeeded));
        upgradeLore.add(Utils.addResourceLine("Nails", barn.getItem(nailKey), toolsNeeded));
        upgradeLore.add(Utils.addResourceLine("Paint buckets", barn.getItem(paintKey), toolsNeeded));

        upgrade.setLore(upgradeLore);
        upgrade.setName(Msg.format("<white>Barn level: %d", barn.getBarnUpgrade().getId()));
        player.getInventory().setItem(8, upgrade);

        TowncraftItemStack backButton = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
        backButton.setName(Msg.format("<red>Click to go back!"));
        player.getInventory().setItem(0, backButton);
    }
}
