package me.webhead1104.towncraft;

import lombok.Getter;
import me.webhead1104.towncraft.commands.TowncraftCommand;
import me.webhead1104.towncraft.impl.TowncraftPlayerPaperImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class TowncraftPaper extends JavaPlugin implements Listener {
    @Getter
    private static TowncraftPaper instance;

    @Override
    public void onEnable() {
        instance = this;
        TowncraftPlatformManager.init();
        TowncraftPlatformManager.getInventoryManager().enable();

        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
                .parameterTypes(TowncraftPlatformManager::registerParameterTypes).build();
        lamp.register(new TowncraftCommand());
        TowncraftPlatformManager.initCommands(lamp);

        registerListeners();
    }

    @Override
    public void onDisable() {
        TowncraftPlatformManager.shutdown();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new IFListenerPaper(TowncraftPlatformManager.getViewFrame()), this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        TowncraftPlatformManager.onJoin(new TowncraftPlayerPaperImpl(event.getPlayer()));
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeave(PlayerQuitEvent event) {
        TowncraftPlatformManager.onLeave(new TowncraftPlayerPaperImpl(event.getPlayer()));
    }
}
