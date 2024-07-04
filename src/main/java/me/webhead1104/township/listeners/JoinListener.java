package me.webhead1104.township.listeners;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.Database;
import net.cytonic.cytosis.data.DatabaseTemplate;
import net.cytonic.cytosis.logging.Logger;
import net.minestom.server.event.player.PlayerSpawnEvent;
import java.sql.SQLException;

public class JoinListener {

    public JoinListener() {}

    public void onJoin(PlayerSpawnEvent event) {
        DatabaseTemplate.QUERY."SELECT * FROM Township WHERE PlayerUUID = \{event.getPlayer().getUuid().toString()}"
                .whenComplete((rs, throwable) -> {
                    if (throwable != null) {
                        Logger.error("error", throwable);
                    } else {
                        try {
                            if (!rs.next()) Database.newPlayer(event.getPlayer().getUuid());
                        } catch (SQLException e) {
                            Logger.error("error", e);
                        }
                    }
                });
    }
}