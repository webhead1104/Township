package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftTask;
import net.minestom.server.timer.Task;

public record TowncraftTaskImpl(Task task) implements TowncraftTask {
    @Override
    public boolean isCancelled() {
        return !task.isAlive();
    }

    @Override
    public void cancel() {
        task.cancel();
    }
}
