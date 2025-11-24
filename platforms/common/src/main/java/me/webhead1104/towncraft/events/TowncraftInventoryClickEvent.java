package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.ClickType;
import me.webhead1104.towncraft.menus.TowncraftSlotType;
import org.jetbrains.annotations.Nullable;


public class TowncraftInventoryClickEvent extends TowncraftInventoryInteractEvent {
    @Getter
    private final ClickType clickType;
    @Getter
    private final TowncraftSlotType slotType;
    private final int whichSlot;
    @Getter
    private final int rawSlot;
    private TowncraftItemStack current = null;
    private int hotbarKey = -1;

    public TowncraftInventoryClickEvent(TowncraftInventoryView view, TowncraftSlotType type, int slot, ClickType click) {
        super(view);
        this.slotType = type;
        this.rawSlot = slot;
        this.whichSlot = view.convertSlot(slot);
        this.clickType = click;
    }

    public TowncraftInventoryClickEvent(TowncraftInventoryView view, TowncraftSlotType type, int slot, ClickType click, int key) {
        this(view, type, slot, click);
        this.hotbarKey = key;
    }

    public TowncraftItemStack getCursor() {
        return this.getView().getCursor();
    }

    @Nullable
    public TowncraftItemStack getCurrentItem() {
        if (this.slotType == TowncraftSlotType.OUTSIDE) {
            return this.current;
        }
        return this.getView().getItem(this.rawSlot);
    }

    public void setCurrentItem(@Nullable TowncraftItemStack stack) {
        if (this.slotType == TowncraftSlotType.OUTSIDE) {
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

    @Nullable
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
