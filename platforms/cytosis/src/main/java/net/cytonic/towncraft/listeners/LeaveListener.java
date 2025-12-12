package net.cytonic.towncraft.listeners;

import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import net.cytonic.cytosis.events.api.Listener;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.event.player.PlayerDisconnectEvent;

public class LeaveListener {

    @Listener
    public void onLeave(PlayerDisconnectEvent event) {
        TowncraftPlatformManager.onLeave(new TowncraftPlayerImpl((CytosisPlayer) event.getPlayer()));
    }
}
