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
package me.webhead1104.towncraft.features.world;

import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.context.RenderContext;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.TowncraftView;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.utils.Msg;
import org.jetbrains.annotations.NotNull;

public class ConfirmCloseMenu extends TowncraftView {

    @Override
    public void onInit(@NotNull ViewConfigBuilder config) {
        config.cancelInteractions();
        config.title(Msg.format("<red>Are you sure?"));
        config.size(1);
    }

    @Override
    public void onFirstRender(@NotNull RenderContext context) {
        context.slot(4).onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.RED_CONCRETE);
            itemStack.setName(Msg.format("<red>Are you sure you want to close towncraft?"));
            itemStack.setLore(
                    Msg.format("<red>If so click this item!"),
                    Msg.format("<green>Or if don't want to close towncraft hit the Esc key or click the back button!")
            );
            slotRenderContext.setItem(itemStack);
        }).onClick(slotClickContext -> {
            userState.get(slotClickContext).save();
            context.closeForEveryone();
            openBackMenu.set(false, slotClickContext);
            TowncraftPlayer player = slotClickContext.getPlayer();
            if (TowncraftPlatformManager.getInventoryManager().getPlayerInventory(player.getUUID()).isPresent()) {
                TowncraftPlatformManager.getInventoryManager().returnItemsToPlayer(player);
            }
        });

        context.slot(8).onRender(slotRenderContext -> {
            TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.BARRIER);
            itemStack.setName(Msg.format("<red>Click to go back!"));
            slotRenderContext.setItem(itemStack);
        }).onClick(SlotClickContext::closeForPlayer);
    }
}
