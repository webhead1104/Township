package me.webhead1104.towncraft.impl.items;

import lombok.EqualsAndHashCode;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;

@EqualsAndHashCode(callSuper = true)
public class TowncraftPlayerInventoryTestImpl extends TowncraftInventoryTestImpl implements TowncraftPlayerInventory {

    public TowncraftPlayerInventoryTestImpl() {
        super(TowncraftInventoryType.PLAYER, 36, Component.text("Player Inventory"));
    }
}
