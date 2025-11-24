package me.webhead1104.towncraft.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.data.objects.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@NoArgsConstructor
public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = TowncraftPlatformManager.getUserManager().getUser(player.getUniqueId());
        user.save();
        TowncraftPlatformManager.getUserManager().removeUser(player.getUniqueId());
        Towncraft.getLogger().info("player {} has left. data has been saved!", player.getName());
    }
}
