package me.webhead1104.towncraft.impl;

import lombok.Getter;
import me.webhead1104.towncraft.*;
import me.webhead1104.towncraft.managers.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

public class TowncraftPaperPlatform implements TowncraftPlatform {
    @Getter
    private static UserManager userManager;
    @Getter
    private static InventoryManager inventoryManager;

    @Override
    public void init() {
        userManager = new UserManager();
        inventoryManager = new InventoryManager();
    }

    @Override
    public TowncraftPlayer getPlayer(UUID uuid) {
        return new TowncraftPlayerImpl(Bukkit.getPlayer(uuid));
    }

    @Override
    public Logger getLogger() {
        return TowncraftPaper.getInstance().getComponentLogger();
    }

    @Override
    public File getDataFolder() {
        return TowncraftPaper.getInstance().getDataFolder();
    }

    @Override
    public void runTaskNextTick(Runnable runnable) {
        Bukkit.getScheduler().runTask(TowncraftPaper.getInstance(), runnable);
    }

    @Override
    public TowncraftTask runTimer(Runnable runnable, long delay, long period) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(TowncraftPaper.getInstance(), runnable, delay, period);
        return new TowncraftTaskImpl(task);
    }
}
