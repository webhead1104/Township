package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftInventoryFactory;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryPaperImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryHolder;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class TowncraftInventoryFactoryPaperImpl implements TowncraftInventoryFactory {
    @Override
    public TowncraftInventory create(TowncraftInventoryHolder holder, TowncraftInventoryType type, Component title) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.valueOf(type.name()), title);
        return new TowncraftInventoryPaperImpl(inventory);
    }

    @Override
    public TowncraftInventory create(TowncraftInventoryHolder holder, int size, Component title) {
        Inventory inventory = Bukkit.createInventory(null, size, title);
        return new TowncraftInventoryPaperImpl(inventory);
    }
}
