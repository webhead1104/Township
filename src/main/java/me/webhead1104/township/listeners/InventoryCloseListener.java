package me.webhead1104.township.listeners;

import me.webhead1104.township.Township;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            Player p = (Player) e.getPlayer();
            if (!Township.getUserManager().userExists(p.getUniqueId())) return;
            if (Township.getUserManager().hasPlayerCloseHandler(p.getUniqueId())) {
                Township.getUserManager().getPlayerCloseHandler(p.getUniqueId()).accept(p.getUniqueId());
                Township.getUserManager().removePlayerCloseHandler(p.getUniqueId());
            }
        }, 1);
    }
}
