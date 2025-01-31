package me.webhead1104.township.listeners;

import me.webhead1104.township.Township;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitTask;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        Bukkit.getScheduler().runTaskLater(Township.getInstance(), () -> {
            if (!Township.getUserManager().userExists(player.getUniqueId())) return;
            if (Township.getUserManager().hasPlayerCloseHandler(player.getUniqueId())) {
                Township.getUserManager().getPlayerCloseHandler(player.getUniqueId()).accept(player.getUniqueId());
                Township.getUserManager().removePlayerCloseHandler(player.getUniqueId());
            }
        }, 1);
        BukkitTask task = Township.getUserManager().menuTasks.get(player.getUniqueId());
        if (task != null) {
            Township.getUserManager().menuTasks.get(player.getUniqueId()).cancel();
            Township.getUserManager().menuTasks.remove(player.getUniqueId());
        }
    }
}
