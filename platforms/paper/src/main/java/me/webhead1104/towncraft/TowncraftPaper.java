package me.webhead1104.towncraft;

import lombok.Getter;
import me.webhead1104.towncraft.commands.TowncraftCommand;
import me.webhead1104.towncraft.listeners.IFListener;
import me.webhead1104.towncraft.listeners.JoinListener;
import me.webhead1104.towncraft.listeners.LeaveListener;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;

public class TowncraftPaper extends JavaPlugin {
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
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new IFListener(TowncraftPlatformManager.getViewFrame()), this);
    }
}
