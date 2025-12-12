package me.webhead1104.towncraft.events;

import lombok.Getter;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.ClickType;

@Getter
public class TowncraftInventoryClickEvent extends TowncraftInventoryInteractEvent {
    private final ClickType clickType;
    private final int slot;
    private final TowncraftItemStack clickedItem;
    private final int hotbarKey;
    private final TowncraftInventory clickedInventory;

    public TowncraftInventoryClickEvent(TowncraftInventoryView view, int slot, ClickType click, int hotbarKey, TowncraftItemStack clickedItem, TowncraftInventory clickedInventory) {
        super(view);
        this.slot = slot;
        this.clickType = click;
        this.hotbarKey = hotbarKey;
        this.clickedItem = clickedItem;
        this.clickedInventory = clickedInventory;
    }
}
