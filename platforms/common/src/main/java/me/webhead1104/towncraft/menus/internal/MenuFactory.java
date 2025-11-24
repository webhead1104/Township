package me.webhead1104.towncraft.menus.internal;

import me.devnatan.inventoryframework.ViewType;
import me.webhead1104.towncraft.factories.TowncraftInventoryFactory;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryHolder;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;

public class MenuFactory {
    public static TowncraftInventory createInventory(TowncraftInventoryHolder holder, ViewType type, int size, Object title) {
        Component component = title instanceof Component ? (Component) title : Component.text((String) title);
        if (!type.isExtendable()) {
            return TowncraftInventoryFactory.INSTANCE.create(holder, toInventoryType(type), component);
        }
        if (size == 0) {
            return TowncraftInventoryFactory.INSTANCE.create(holder, toInventoryType(type), component);
        }
        return TowncraftInventoryFactory.INSTANCE.create(holder, size, component);
    }

    private static TowncraftInventoryType toInventoryType(ViewType type) {
        return TowncraftInventoryType.valueOf(type.getIdentifier().replaceAll("-", "_").toUpperCase());
    }
}
