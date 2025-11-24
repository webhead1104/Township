package me.webhead1104.towncraft;

import lombok.Getter;
import me.webhead1104.towncraft.commands.TowncraftCommand;
import me.webhead1104.towncraft.commands.TowncraftPlayerSourceResolver;
import me.webhead1104.towncraft.listeners.IFListener;
import me.webhead1104.towncraft.listeners.JoinListener;
import me.webhead1104.towncraft.listeners.LeaveListener;
import org.bukkit.plugin.java.JavaPlugin;
import studio.mevera.imperat.BukkitImperat;

public class TowncraftPaper extends JavaPlugin {
    @Getter
    private static TowncraftPaper instance;

    @Override
    public void onEnable() {
        instance = this;
        TowncraftPlatformManager.init();
        saveDefaultConfig();
        BukkitImperat imperat = BukkitImperat.builder(this)
                .applyBrigadier(true)
                .sourceResolver(TowncraftPlayer.class, new TowncraftPlayerSourceResolver<>())
                .build();

        imperat.registerCommand(new TowncraftCommand());
        registerListeners();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new IFListener(TowncraftPlatformManager.getViewFrame()), this);
    }
}
