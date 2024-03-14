package me.webhead1104.township.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import me.webhead1104.township.Items;
import me.webhead1104.township.Township;

public class InventoryClickListener implements Listener {

    Township plugin;
    Items items;
    public InventoryClickListener(Township plugin) {
        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    @EventHandler
    public void onJoin(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null) {
            if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Level ")) {
                plugin.getCommand().levelMenu(player);
            }
            if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Level ")) {
            }
        }
    }
}
