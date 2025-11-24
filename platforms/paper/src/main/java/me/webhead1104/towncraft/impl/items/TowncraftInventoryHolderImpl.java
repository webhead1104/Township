package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.items.TowncraftInventoryHolder;
import org.bukkit.inventory.InventoryHolder;

public record TowncraftInventoryHolderImpl(InventoryHolder holder) implements TowncraftInventoryHolder {
    @Override
    public Object getInventory() {
        return holder.getInventory();
    }
}
