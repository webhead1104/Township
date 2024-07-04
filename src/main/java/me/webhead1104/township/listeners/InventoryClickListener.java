package me.webhead1104.township.listeners;

import me.webhead1104.township.Township;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

public class InventoryClickListener {

    public InventoryClickListener() {
    }

    public void onClick(InventoryPreClickEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getClickedItem();
        //up
        if (item.toItemNBT().getString("arrow", "none").equals("up"))
            Township.getWorldManager().getWorld(player, Township.getUserPageMap().get(player.getUuid()) + 1);
        //down
        if (item.toItemNBT().getString("arrow", "none").equals("down"))
            Township.getWorldManager().getWorld(player, Township.getUserPageMap().get(player.getUuid()) - 1);
        //left
        if (item.toItemNBT().getString("arrow", "none").equals("left"))
            Township.getWorldManager().getWorld(player, Township.getUserPageMap().get(player.getUuid()) + 2);
        //right
        if (item.toItemNBT().getString("arrow", "none").equals("right"))
            Township.getWorldManager().getWorld(player, Township.getUserPageMap().get(player.getUuid()) - 2);
    }
}