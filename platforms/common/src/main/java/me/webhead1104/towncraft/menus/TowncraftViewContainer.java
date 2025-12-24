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
package me.webhead1104.towncraft.menus;

import com.google.common.collect.Sets;
import lombok.Getter;
import me.devnatan.inventoryframework.ViewContainer;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.Viewer;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public final class TowncraftViewContainer implements ViewContainer {
    private static final Set<String> UNOPENABLES = Sets.newHashSet("CRAFTING", "CREATIVE", "PLAYER");
    @Getter
    private final TowncraftInventory inventory;
    @Getter
    private final boolean shared;
    private final ViewType type;
    private final boolean proxied;

    public TowncraftViewContainer(@NotNull TowncraftInventory inventory, boolean shared, ViewType type, boolean proxied) {
        this.inventory = inventory;
        this.shared = shared;
        this.type = type;
        this.proxied = proxied;
    }

    @Override
    public boolean isProxied() {
        return proxied;
    }

    @Override
    public String getTitle() {
        final boolean diffTitle = inventory.getViewers().stream()
                .map(TowncraftPlayer::getOpenInventory)
                .map(TowncraftInventory::getTitle)
                .distinct()
                .findAny()
                .isPresent();

        if (diffTitle && shared) throw new IllegalStateException("Cannot get unique title of shared inventory");

        return inventory.getViewers().getFirst().getOpenInventory().getTitleString();
    }

    @Override
    public String getTitle(@NotNull Viewer viewer) {
        return ((TowncraftViewer) viewer).getPlayer().getOpenInventory().getTitleString();
    }

    @Override
    public @NotNull ViewType getType() {
        return type;
    }

    @Override
    public int getRowsCount() {
        return getSize() / getColumnsCount();
    }

    @Override
    public int getColumnsCount() {
        return type.getColumns();
    }

    @Override
    public void renderItem(int slot, Object item) {
        requireSupportedItem(item);
        inventory.setItem(slot, (TowncraftItemStack) item);
    }

    @Override
    public void removeItem(int slot) {
        inventory.clear(slot);
    }

    @Override
    public boolean matchesItem(int slot, Object item, boolean exactly) {
        requireSupportedItem(item);
        final TowncraftItemStack target = inventory.getItem(slot);
        if (target == null) return item == null;
        if (item instanceof TowncraftItemStack)
            return exactly ? target.equals(item) : target.isSimilar((TowncraftItemStack) item);

        return false;
    }

    @Override
    public boolean isSupportedItem(Object item) {
        return item == null || item instanceof TowncraftItemStack;
    }

    private void requireSupportedItem(Object item) {
        if (isSupportedItem(item)) return;

        throw new IllegalStateException(
                "Unsupported item type: " + item.getClass().getName());
    }

    @Override
    public boolean hasItem(int slot) {
        try {
            return inventory.getItem(slot) != null;
        } catch (final ArrayIndexOutOfBoundsException ignored) {
            // just supress AIOOBE here, we cannot check if slot matches container constraints
            // by `size >= 0 && size <= getLastSlot()` because some containers are not aligned.
            // Aligned inventory types = perfect grid (NxN) like chest, workbench..
            return false;
        }
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public int getSlotsCount() {
        return getSize() - 1;
    }

    @Override
    public int getFirstSlot() {
        return 0;
    }

    @Override
    public int getLastSlot() {
        final int[] resultSlots = getType().getResultSlots();
        int lastSlot = getSlotsCount();
        if (resultSlots != null) {
            for (final int resultSlot : resultSlots) {
                if (resultSlot == lastSlot) lastSlot--;
            }
        }

        return lastSlot;
    }

    @Override
    public void changeTitle(@Nullable Object title, @NotNull Viewer target) {
        changeTitle(title, ((TowncraftViewer) target).getPlayer());
    }

    public void changeTitle(@Nullable Object title, @NotNull TowncraftPlayer player) {
        TowncraftInventory inventory = player.getOpenInventory();
        if (UNOPENABLES.contains(inventory.getType().name())) return;
        if (title instanceof Component) {
            inventory.setTitle((Component) title);
            return;
        }
        inventory.setTitle((String) title);
    }

    @Override
    public boolean isEntityContainer() {
        return inventory instanceof TowncraftPlayerInventory;
    }

    @Override
    public void open(@NotNull final Viewer viewer) {
        viewer.open(this);
    }

    @Override
    public void close() {
        new ArrayList<>(inventory.getViewers()).forEach(TowncraftPlayer::closeInventory);
    }

    @Override
    public void close(@NotNull Viewer viewer) {
        viewer.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TowncraftViewContainer that = (TowncraftViewContainer) o;
        return shared == that.shared
                && Objects.equals(inventory, that.inventory)
                && Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(inventory, shared, getType());
    }

    @Override
    public String toString() {
        return "TowncraftViewContainer{" + "inventory=" + inventory + ", shared=" + shared + ", type=" + type + '}';
    }
}
