package me.webhead1104.towncraft.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPaper;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.database.UserLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;

@NoArgsConstructor
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        long start = System.currentTimeMillis();
        Player player = event.getPlayer();
        UserLoader userLoader = Towncraft.getUserLoader();
        Bukkit.getScheduler().runTaskAsynchronously(TowncraftPaper.getInstance(), () -> {
            try {
                if (userLoader.userExists(player.getUniqueId())) {
                    User user = User.fromJson(userLoader.readUser(player.getUniqueId()));
                    TowncraftPlatformManager.getUserManager().setUser(user.getUuid(), user);
                    Towncraft.getLogger().info("player {} has joined. data has been loaded!", player.getName());
                } else {
                    Towncraft.getLogger().info("player {} has joined. data not found. creating new player...", player.getName());
                    User user = new User(player.getUniqueId());
                    userLoader.saveUser(player.getUniqueId(), user.toString());
                    TowncraftPlatformManager.getUserManager().setUser(player.getUniqueId(), user);
                }
            } catch (IOException e) {
                Towncraft.getLogger().error("An error occurred whilst loading player data!", e);
            }
            Towncraft.getLogger().info("Join event done in {} mills", System.currentTimeMillis() - start);
        });
    }
}