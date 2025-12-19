package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import org.bukkit.inventory.PlayerInventory;

public class TowncraftPlayerInventoryPaperImpl extends TowncraftInventoryPaperImpl implements TowncraftPlayerInventory {
    public TowncraftPlayerInventoryPaperImpl(PlayerInventory playerInventory) {
        super(playerInventory);
    }
}
