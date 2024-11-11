package me.webhead1104.township.listeners;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@NoArgsConstructor
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        long start = System.currentTimeMillis();
        Player player = event.getPlayer();

        Township.getDatabase().hasPlayerJoinedBefore(player.getUniqueId()).whenComplete((value, throwable) -> {
            if (throwable != null) {
                Township.logger.error("An error occurred whilst checking if the player had joined before!", throwable);
                return;
            }
            if (value) {
                Township.getDatabase().getUser(player.getUniqueId()).whenComplete((user, throwable1) -> {
                    if (throwable1 != null) {
                        Township.logger.error("An error occurred whilst loading player data!", throwable1);
                        return;
                    }
                    Township.getUserManager().setUser(user.getUuid(), user);
                    Township.logger.info(STR."player \{event.getPlayer().getName()} has joined. data has been loaded!");
                });
            } else {
                Township.logger.info(STR."player \{event.getPlayer().getName()} has joined. data not found. creating new player...");
                Township.getDatabase().newPlayer(event.getPlayer().getUniqueId());
            }
        });
        Township.logger.info(STR."Join event done in \{System.currentTimeMillis() - start} mills");
    }
}
