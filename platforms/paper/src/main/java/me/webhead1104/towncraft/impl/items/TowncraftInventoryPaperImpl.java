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
package me.webhead1104.towncraft.impl.items;

import lombok.NonNull;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerPaperImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TowncraftInventoryPaperImpl implements TowncraftInventory {
    private final Inventory inventory;

    public TowncraftInventoryPaperImpl(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return inventory.getViewers().stream()
                .map(TowncraftPlayerPaperImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public Component getTitle() {
        return inventory.getType().defaultTitle();
    }

    @Override
    public void setTitle(Component title) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public TowncraftInventoryType getType() {
        return TowncraftInventoryType.valueOf(inventory.getType().name());
    }

    @Override
    public void setItem(int slot, @NonNull TowncraftItemStack itemStack) {
        inventory.setItem(slot, (ItemStack) itemStack.toPlatform());
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        return new TowncraftItemStackPaperImpl(inventory.getItem(slot));
    }

    @Override
    public @Nullable TowncraftItemStack @NotNull [] getContents() {
        return Arrays.stream(inventory.getContents())
                .map(TowncraftItemStackPaperImpl::new).toArray(TowncraftItemStackPaperImpl[]::new);
    }

    @Override
    public void setContents(@Nullable TowncraftItemStack @NotNull [] items) {
        ItemStack[] itemStacks = new ItemStack[items.length];
        int i = 0;
        for (TowncraftItemStack item : items) {
            if (item == null) {
                itemStacks[i++] = null;
                continue;
            }
            itemStacks[i++] = (ItemStack) item.toPlatform();
        }
        inventory.setContents(itemStacks);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void clear(int slot) {
        inventory.clear(slot);
    }

    @Override
    public Object getPlatform() {
        return inventory;
    }
}
