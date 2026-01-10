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

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(exclude = "viewers")
public class TowncraftInventoryTestImpl implements TowncraftInventory {
    private final List<TowncraftPlayer> viewers = new ArrayList<>();
    private final TowncraftInventoryType inventoryType;
    private final int size;
    private @Nullable TowncraftItemStack @NotNull [] contents;
    private Component title;

    public TowncraftInventoryTestImpl(@NotNull TowncraftInventoryType inventoryType, int size, @NotNull Component title) {
        Preconditions.checkNotNull(inventoryType, "inventoryType cannot be null");
        Preconditions.checkArgument(size > 0, "Inventory size has to be > 0");
        Preconditions.checkNotNull(title, "Title cannot be null");
        this.inventoryType = inventoryType;
        this.size = size;
        this.title = title;
        this.contents = new TowncraftItemStack[size + 1];
        Arrays.fill(contents, TowncraftItemStack.empty());
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return viewers;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public void setTitle(Component title) {
        this.title = title;
    }

    @Override
    public TowncraftInventoryType getType() {
        return inventoryType;
    }

    @Override
    public void setItem(int slot, TowncraftItemStack itemStack) {
        if (slot < 0 || slot > size) {
            throw new IllegalArgumentException("Slot must be between 0 and " + size);
        }
        contents[slot] = itemStack;
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        if (slot < 0 || slot > size) {
            throw new IllegalArgumentException("Slot must be between 0 and " + size);
        }
        return contents[slot];
    }

    @Override
    public @Nullable TowncraftItemStack @NotNull [] getContents() {
        return contents;
    }

    @Override
    public void setContents(@Nullable TowncraftItemStack @NotNull [] items) {
        this.contents = items;
    }

    @Override
    public void clear() {
        Arrays.fill(this.contents, TowncraftItemStack.empty());
    }

    @Override
    public void clear(int slot) {
        this.contents[slot] = TowncraftItemStack.empty();
    }

    @Override
    public Object getPlatform() {
        throw new UnsupportedOperationException();
    }
}
