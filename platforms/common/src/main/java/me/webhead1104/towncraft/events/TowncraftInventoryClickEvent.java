package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.ClickType;
import org.jetbrains.annotations.Nullable;

public class TowncraftInventoryClickEvent extends TowncraftInventoryInteractEvent {
    @Getter
    private final ClickType clickType;
    @Getter
    private final boolean outside;
    private final int whichSlot;
    @Getter
    private final int rawSlot;
    private final int hotbarKey;
    private TowncraftItemStack current = null;

    public TowncraftInventoryClickEvent(TowncraftInventoryView view, boolean outside, int slot, ClickType click, int key) {
        super(view);
        this.outside = outside;
        this.rawSlot = slot;
        this.whichSlot = view.convertSlot(slot);
        Towncraft.getLogger().info("whichSlot = {} rawSlot = {}", whichSlot, rawSlot);
        this.clickType = click;
        this.hotbarKey = key;
    }

    public TowncraftInventoryClickEvent(TowncraftInventoryView view, boolean outside, int slot, int whichSlot, ClickType click, int key) {
        super(view);
        this.outside = outside;
        this.rawSlot = slot;
        this.whichSlot = whichSlot;
        Towncraft.getLogger().info("whichSlot = {} rawSlot = {}", whichSlot, rawSlot);
        this.clickType = click;
        this.hotbarKey = key;
    }

    public TowncraftItemStack getCursor() {
        return this.getView().getCursor();
    }

    @Nullable
    public TowncraftItemStack getCurrentItem() {
        if (outside) {
            return this.current;
        }
        return this.getView().getItem(this.rawSlot);
    }

    public void setCurrentItem(@Nullable TowncraftItemStack stack) {
        if (outside) {
            this.current = stack;
        } else {
            getView().setItem(this.rawSlot, stack);
        }
    }

    public boolean isRightClick() {
        return this.clickType.isRightClick();
    }

    public boolean isLeftClick() {
        return this.clickType.isLeftClick();
    }

    public boolean isShiftClick() {
        return this.clickType.isShiftClick();
    }

    public TowncraftInventory getClickedInventory() {
        return this.getView().getInventory(rawSlot);
    }

    public int getSlot() {
        return this.whichSlot;
    }

    public int getHotbarButton() {
        return this.hotbarKey;
    }
}
