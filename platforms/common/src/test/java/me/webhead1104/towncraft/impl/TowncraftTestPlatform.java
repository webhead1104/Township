package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftPlatform;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.TowncraftTask;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TowncraftTestPlatform implements TowncraftPlatform {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public @Nullable TowncraftPlayer getPlayer(UUID uuid) {
        return null;
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger("Towncraft");
    }

    @Override
    public File getDataFolder() {
        return new File("build/config/config");
    }

    @Override
    public void runTaskNextTick(Runnable runnable) {
        scheduler.schedule(runnable, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public TowncraftTask runTimer(Runnable runnable, long delay, long period) {
        return null;
    }

    @Override
    public TowncraftTask runTaskAsync(Runnable runnable) {
        return null;
    }
}
