package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.database.UserLoader;
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
        UserLoader userLoader = Township.getLoaderManager().getUserLoader();
        Bukkit.getScheduler().runTaskAsynchronously(Township.getInstance(), () -> {
            try {
                if (userLoader.userExists(player.getUniqueId())) {
                    User user = User.fromJson(userLoader.readUser(player.getUniqueId()));
                    Township.getUserManager().setUser(user.getUuid(), user);
                    Township.logger.info("player {} has joined. data has been loaded!", player.getName());
                } else {
                    Township.logger.info("player {} has joined. data not found. creating new player...", player.getName());
                    User user = new User(player.getUniqueId());
                    userLoader.saveUser(player.getUniqueId(), user.toString());
                    Township.getUserManager().setUser(player.getUniqueId(), user);
                }
            } catch (IOException e) {
                Township.logger.error("An error occurred whilst loading player data!", e);
            }
            Township.logger.info("Join event done in {} mills", System.currentTimeMillis() - start);
        });
    }
}