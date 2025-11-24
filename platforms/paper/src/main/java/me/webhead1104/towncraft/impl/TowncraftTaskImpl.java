package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftTask;
import org.bukkit.scheduler.BukkitTask;

public record TowncraftTaskImpl(BukkitTask task) implements TowncraftTask {
    @Override
    public boolean isCancelled() {
        return task.isCancelled();
    }

    @Override
    public void cancel() {
        task.cancel();
    }
}
