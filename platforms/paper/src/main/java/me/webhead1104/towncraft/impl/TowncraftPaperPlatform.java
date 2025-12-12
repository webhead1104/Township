package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftPaper;
import me.webhead1104.towncraft.TowncraftPlatform;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.TowncraftTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

import java.io.File;
import java.util.UUID;

public class TowncraftPaperPlatform implements TowncraftPlatform {

    @Override
    @Nullable
    public TowncraftPlayer getPlayer(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            return null;
        }
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

    @Override
    public TowncraftTask runTaskAsync(Runnable runnable) {
        BukkitTask task = Bukkit.getScheduler().runTaskAsynchronously(TowncraftPaper.getInstance(), runnable);
        return new TowncraftTaskImpl(task);
    }
}
