package me.webhead1104.towncraft.listeners;

import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        TowncraftPlatformManager.onLeave(new TowncraftPlayerImpl(event.getPlayer()));
    }
}
