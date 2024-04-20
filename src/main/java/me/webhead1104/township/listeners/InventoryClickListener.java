package me.webhead1104.township.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import me.webhead1104.township.Township;

public class InventoryClickListener implements Listener {

    Township plugin;
    public InventoryClickListener(Township plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null) {
            if (clickedItem.hasItemMeta()) {
                if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.AQUA + "Level ")) plugin.getLevelMenu().levelMenu(player);
                //up
                if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Click to scroll up!")) plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()+1);
                //down
                if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Click to scroll down!")) plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()-1);
                //left
                if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Click to scroll left!")) plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()+2);
                //right
                if (clickedItem.getItemMeta().getDisplayName().contains(ChatColor.DARK_GREEN + "Click to scroll right!")) plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()-2);
            }
        }
    }
}