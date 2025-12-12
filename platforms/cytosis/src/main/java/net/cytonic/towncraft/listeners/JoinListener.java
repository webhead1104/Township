package net.cytonic.towncraft.listeners;

import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import net.cytonic.cytosis.events.api.Listener;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.event.player.PlayerSpawnEvent;

public class JoinListener {

    @Listener
    public void onJoin(PlayerSpawnEvent event) {
        TowncraftPlatformManager.onJoin(new TowncraftPlayerImpl((CytosisPlayer) event.getPlayer()));
    }
}