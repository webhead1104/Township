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
package me.devnatan.inventoryframework.context;

import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfig;
import me.devnatan.inventoryframework.ViewContainer;
import me.devnatan.inventoryframework.Viewer;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryHolder;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftViewContainer;
import me.webhead1104.towncraft.menus.TowncraftViewer;
import me.webhead1104.towncraft.menus.component.TowncraftItemComponentBuilder;
import me.webhead1104.towncraft.menus.context.Context;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class RenderContext extends PlatformRenderContext<TowncraftItemComponentBuilder, Context>
        implements Context, TowncraftInventoryHolder {

    private final TowncraftPlayer player;

    /**
     * <b><i> This is an internal inventory-framework API that should not be used from outside of
     * this library. No compatibility guarantees are provided. </i></b>
     */
    @ApiStatus.Internal
    public RenderContext(
            @NotNull UUID id,
            @NotNull View root,
            @NotNull ViewConfig config,
            ViewContainer container,
            @NotNull Map<String, Viewer> viewers,
            Viewer subject,
            Object initialData) {
        super(id, root, config, container, viewers, subject, initialData);
        this.player = subject != null ? ((TowncraftViewer) subject).getPlayer() : null;
    }

    @Override
    public @NotNull View getRoot() {
        return (View) root;
    }

    public @NotNull TowncraftPlayer getPlayer() {
        tryThrowDoNotWorkWithSharedContext("getAllPlayers");
        return player;
    }

    public @NotNull User getUser() {
        tryThrowDoNotWorkWithSharedContext("getUser");
        return player.getUser();
    }

    @Override
    public List<TowncraftPlayer> getAllPlayers() {
        return getViewers().stream()
                .map(viewer -> (TowncraftViewer) viewer)
                .map(TowncraftViewer::getPlayer)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTitleForPlayer(@NotNull String title, @NotNull TowncraftPlayer player) {
        ((TowncraftViewContainer) getContainer()).changeTitle(title, player);
    }

    @Override
    public void resetTitleForPlayer(@NotNull TowncraftPlayer player) {
        ((TowncraftViewContainer) getContainer()).changeTitle(null, player);
    }

    /**
     * Adds an item to a specific slot in the context container.
     *
     * @param slot The slot in which the item will be positioned.
     * @return An item builder to configure the item.
     */
    public @NotNull TowncraftItemComponentBuilder slot(int slot, @Nullable TowncraftItemStack item) {
        return slot(slot).withItem(item);
    }

    /**
     * Adds an item at the specific column and ROW (X, Y) in that context's container.
     *
     * @param row    The row (Y) in which the item will be positioned.
     * @param column The column (X) in which the item will be positioned.
     * @return An item builder to configure the item.
     */
    @NotNull
    public TowncraftItemComponentBuilder slot(int row, int column, @Nullable TowncraftItemStack item) {
        return slot(row, column).withItem(item);
    }

    /**
     * Sets an item in the first slot of this context's container.
     *
     * @param item The item that'll be set.
     * @return An item builder to configure the item.
     */
    public @NotNull TowncraftItemComponentBuilder firstSlot(@Nullable TowncraftItemStack item) {
        return firstSlot().withItem(item);
    }

    /**
     * Sets an item in the last slot of this context's container.
     *
     * @param item The item that'll be set.
     * @return An item builder to configure the item.
     */
    public @NotNull TowncraftItemComponentBuilder lastSlot(@Nullable TowncraftItemStack item) {
        return lastSlot().withItem(item);
    }

    /**
     * Adds an item in the next available slot of this context's container.
     *
     * @param item The item that'll be added.
     * @return An item builder to configure the item.
     */
    public @NotNull TowncraftItemComponentBuilder availableSlot(@Nullable TowncraftItemStack item) {
        return availableSlot().withItem(item);
    }

    /**
     * Defines the item that will represent a character provided in the context layout.
     *
     * @param character The layout character target.
     * @param item      The item that'll represent the layout character.
     * @return An item builder to configure the item.
     */
    public @NotNull TowncraftItemComponentBuilder layoutSlot(char character, @Nullable TowncraftItemStack item) {
        return layoutSlot(character).withItem(item);
    }

    /**
     * <p><b><i> This API is experimental and is not subject to the general compatibility guarantees
     * such API may be changed or may be removed completely in any further release. </i></b>
     */
    @ApiStatus.Experimental
    public @NotNull TowncraftItemComponentBuilder resultSlot(@Nullable TowncraftItemStack item) {
        return resultSlot().withItem(item);
    }

    @Override
    protected TowncraftItemComponentBuilder createBuilder() {
        return new TowncraftItemComponentBuilder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RenderContext that = (RenderContext) o;
        return Objects.equals(getPlayer(), that.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPlayer());
    }

    @Override
    public String toString() {
        return "RenderContext{" + "player=" + player + "} " + super.toString();
    }

    @Override
    public TowncraftInventory getInventory() {
        return ((TowncraftViewContainer) getContainerOrThrow()).getInventory();
    }
}