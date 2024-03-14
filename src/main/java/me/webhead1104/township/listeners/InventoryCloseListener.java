package me.webhead1104.township.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import me.webhead1104.township.Township;

public class InventoryCloseListener implements Listener {

    Township plugin;
    public InventoryCloseListener(Township plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(InventoryClickEvent event) {
        if (event.getInventory().equals(plugin.getWorldManager().worldMenu.getInventory())) {}
    }
}
