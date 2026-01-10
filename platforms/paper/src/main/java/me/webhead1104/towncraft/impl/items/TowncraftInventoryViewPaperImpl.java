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
package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerPaperImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.stream.Collectors;

public record TowncraftInventoryViewPaperImpl(InventoryView view) implements TowncraftInventoryView {
    @Override
    public TowncraftInventory getTopInventory() {
        return new TowncraftInventoryPaperImpl(view.getTopInventory());
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return view.getTopInventory().getViewers()
                .stream().map(TowncraftPlayerPaperImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public int convertSlot(int slot) {
        return view.convertSlot(slot);
    }

    @Override
    public TowncraftPlayer getPlayer() {
        return new TowncraftPlayerPaperImpl(view.getPlayer());
    }

    @Override
    public TowncraftItemStack getCursor() {
        return new TowncraftItemStackPaperImpl(view.getCursor());
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        return new TowncraftItemStackPaperImpl(view.getItem(slot));
    }

    @Override
    public void setItem(int slot, TowncraftItemStack item) {
        view.setItem(slot, (ItemStack) item.toPlatform());
    }

    @Override
    public TowncraftInventory getInventory(int slot) {
        if (view.getInventory(slot) instanceof PlayerInventory playerInventory) {
            return new TowncraftPlayerInventoryPaperImpl(playerInventory);
        }
        return new TowncraftInventoryPaperImpl(view.getInventory(slot));
    }
}
