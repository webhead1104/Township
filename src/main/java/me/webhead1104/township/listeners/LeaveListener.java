package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

@NoArgsConstructor
public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Township.getDatabase().setData(user);
        Township.getUserManager().removeUser(player.getUniqueId());
        BukkitTask task = Township.getUserManager().menuTasks.get(player.getUniqueId());
        if (task != null) {
            Township.getUserManager().menuTasks.get(player.getUniqueId()).cancel();
            Township.getUserManager().menuTasks.remove(player.getUniqueId());
        }
        Township.logger.info("player {} has left. data has been saved!", player.getName());
    }
}
