package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.minestom.server.inventory.PlayerInventory;

public class TowncraftPlayerInventoryImpl extends TowncraftInventoryImpl implements TowncraftPlayerInventory {
    public TowncraftPlayerInventoryImpl(PlayerInventory playerInventory) {
        super(playerInventory);
    }
}
