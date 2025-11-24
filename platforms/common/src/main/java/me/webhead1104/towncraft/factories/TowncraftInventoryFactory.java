package me.webhead1104.towncraft.factories;

import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryHolder;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.Services;

public interface TowncraftInventoryFactory {
    TowncraftInventoryFactory INSTANCE = Services.service(TowncraftInventoryFactory.class)
            .orElseThrow(() -> new IllegalStateException("No TowncraftInventoryFactory found!"));

    default TowncraftInventory create(TowncraftInventoryHolder holder, TowncraftInventoryType type, String title) {
        return create(holder, type, Component.text(title));
    }

    TowncraftInventory create(TowncraftInventoryHolder holder, TowncraftInventoryType type, Component title);

    TowncraftInventory create(TowncraftInventoryHolder holder, int size, Component title);

}
