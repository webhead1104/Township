package me.webhead1104.towncraft.menus.context;

import me.devnatan.inventoryframework.RootView;
import me.devnatan.inventoryframework.ViewContainer;
import me.devnatan.inventoryframework.Viewer;
import me.devnatan.inventoryframework.component.Component;
import me.devnatan.inventoryframework.context.IFRenderContext;
import me.devnatan.inventoryframework.context.IFSlotClickContext;
import me.devnatan.inventoryframework.context.SlotContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.events.TowncraftInventoryClickEvent;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import me.webhead1104.towncraft.menus.ClickType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlotClickContext extends SlotContext implements IFSlotClickContext {
    private final Viewer whoClicked;
    private final ViewContainer clickedContainer;
    private final Component clickedComponent;
    private final TowncraftInventoryClickEvent clickOrigin;
    private final boolean combined;
    private boolean cancelled;

    @ApiStatus.Internal
    public SlotClickContext(
            int slot,
            @NotNull IFRenderContext parent,
            @NotNull Viewer whoClicked,
            @NotNull ViewContainer clickedContainer,
            @Nullable Component clickedComponent,
            @NotNull TowncraftInventoryClickEvent clickOrigin,
            boolean combined) {
        super(slot, parent);
        Towncraft.getLogger().info("Created SlotClickContext for slot {}.", slot);
        this.whoClicked = whoClicked;
        this.clickedContainer = clickedContainer;
        this.clickedComponent = clickedComponent;
        this.clickOrigin = clickOrigin;
        this.combined = combined;
    }

    /**
     * The player who clicked on the slot.
     */
    public final @NotNull TowncraftPlayer getPlayer() {
        return clickOrigin.getPlayer();
    }

    public final @NotNull User getUser() {
        return getPlayer().getUser();
    }

    /**
     * The event that triggered this context.
     * <p>
     * This is an internal inventory-framework API that should not be used from outside of
     * this library. No compatibility guarantees are provided.
     */
    @NotNull
    public TowncraftInventoryClickEvent getClickOrigin() {
        return clickOrigin;
    }

    /**
     * The item that was clicked.
     */
    @Override
    public final TowncraftItemStack getItem() {
        return clickOrigin.getCurrentItem();
    }

    @Override
    public final Component getComponent() {
        return clickedComponent;
    }

    @Override
    public final @NotNull ViewContainer getClickedContainer() {
        return clickedContainer;
    }

    @Override
    public final boolean isCancelled() {
        return cancelled;
    }

    @Override
    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        clickOrigin.setCancelled(cancelled);
    }

    @Override
    public final Object getPlatformEvent() {
        return clickOrigin;
    }

    @Override
    public final int getClickedSlot() {
        Towncraft.getLogger().info("Returning clicked slot {}, getSlot = {}", clickOrigin.getSlot(), getSlot());
        return clickOrigin.getRawSlot();
    }

    @Override
    public final boolean isLeftClick() {
        return clickOrigin.isLeftClick();
    }

    @Override
    public final boolean isRightClick() {
        return clickOrigin.getClickType().isRightClick();
    }

    @Override
    public final boolean isMiddleClick() {
        return clickOrigin.getClickType() == ClickType.MIDDLE;
    }

    @Override
    public final boolean isShiftClick() {
        return clickOrigin.getClickType().isShiftClick();
    }

    @Override
    public final boolean isKeyboardClick() {
        return clickOrigin.getClickType().isKeyboardClick();
    }

    @Override
    public final boolean isOutsideClick() {
        return getClickOrigin().isOutside();
    }

    @Override
    public final String getClickIdentifier() {
        return clickOrigin.getClickType().name();
    }

    @Override
    public final boolean isOnEntityContainer() {
        Towncraft.getLogger().info("Clicked inventory class = {}, platform clicked inventory class = {}", clickOrigin.getClickedInventory().getClass(), clickOrigin.getClickedInventory().getPlatform().getClass());
        return clickOrigin.getClickedInventory() instanceof TowncraftPlayerInventory;
    }

    @Override
    public final Viewer getViewer() {
        return whoClicked;
    }

    @Override
    public final void closeForPlayer() {
        getParent().closeForPlayer();
    }

    @Override
    public final void openForPlayer(@NotNull Class<? extends RootView> other) {
        getParent().openForPlayer(other);
    }

    @Override
    public final void openForPlayer(@NotNull Class<? extends RootView> other, Object initialData) {
        getParent().openForPlayer(other, initialData);
    }

    @Override
    public final void updateTitleForPlayer(@NotNull String title) {
        getParent().updateTitleForPlayer(title);
    }

    @Override
    public final void updateTitleForPlayer(@NotNull Object title) {
        getParent().updateTitleForPlayer(title);
    }

    @Override
    public final void resetTitleForPlayer() {
        getParent().resetTitleForPlayer();
    }

    @Override
    public final boolean isCombined() {
        return combined;
    }
}
