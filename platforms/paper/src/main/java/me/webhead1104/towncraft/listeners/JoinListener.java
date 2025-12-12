package me.webhead1104.towncraft.listeners;

import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TowncraftPlatformManager.onJoin(new TowncraftPlayerImpl(event.getPlayer()));
    }
}