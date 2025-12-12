package me.webhead1104.towncraft.impl.factories;

import me.webhead1104.towncraft.factories.TowncraftInventoryFactory;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryHolder;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;

public class TowncraftInventoryFactoryImpl implements TowncraftInventoryFactory {
    @Override
    public TowncraftInventory create(TowncraftInventoryHolder holder, TowncraftInventoryType type, Component title) {
        return new TowncraftInventoryImpl(new Inventory(getInventoryType(type), title));
    }

    @Override
    public TowncraftInventory create(TowncraftInventoryHolder holder, int size, Component title) {
        return new TowncraftInventoryImpl(new Inventory(getInventoryType(size), title));
    }

    private InventoryType getInventoryType(TowncraftInventoryType inventoryType) {
        if (inventoryType == TowncraftInventoryType.CHEST) {
            return InventoryType.CHEST_6_ROW;
        }
        return InventoryType.valueOf(inventoryType.name());
    }

    private InventoryType getInventoryType(int size) {
        return switch (size / 9) {
            case 1 -> InventoryType.CHEST_1_ROW;
            case 2 -> InventoryType.CHEST_2_ROW;
            case 3 -> InventoryType.CHEST_3_ROW;
            case 4 -> InventoryType.CHEST_4_ROW;
            case 5 -> InventoryType.CHEST_5_ROW;
            default -> InventoryType.CHEST_6_ROW;
        };
    }
}
