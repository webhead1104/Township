package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftPlatform;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.TowncraftTask;
import net.cytonic.cytosis.Cytosis;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;

public class TowncraftCytosisPlatform implements TowncraftPlatform {
    private static final Logger LOGGER = LoggerFactory.getLogger("Towncraft");

    @Override
    public TowncraftPlayer getPlayer(UUID uuid) {
        return new TowncraftPlayerImpl(Cytosis.getPlayer(uuid).orElseThrow());
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    public File getDataFolder() {
        return new File("Towncraft");
    }

    @Override
    public void runTaskNextTick(Runnable runnable) {
        MinecraftServer.getSchedulerManager().buildTask(runnable).delay(TaskSchedule.nextTick()).schedule();
    }

    @Override
    public TowncraftTask runTimer(Runnable runnable, long delay, long period) {
        Task task = MinecraftServer.getSchedulerManager().buildTask(runnable).delay(TaskSchedule.tick(Math.toIntExact(delay)))
                .repeat(TaskSchedule.tick(Math.toIntExact(period))).schedule();
        return new TowncraftTaskImpl(task);
    }

    @Override
    public TowncraftTask runTaskAsync(Runnable runnable) {
        Task task = MinecraftServer.getSchedulerManager().buildTask(runnable).schedule();
        return new TowncraftTaskImpl(task);
    }
}
