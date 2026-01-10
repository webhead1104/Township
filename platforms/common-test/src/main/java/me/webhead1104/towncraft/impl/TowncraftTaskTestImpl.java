package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftTask;

import java.util.concurrent.ScheduledFuture;

public record TowncraftTaskTestImpl(ScheduledFuture<?> scheduledFuture) implements TowncraftTask {
    @Override
    public boolean isCancelled() {
        return scheduledFuture.isCancelled();
    }

    @Override
    public void cancel() {
        scheduledFuture.cancel(false);
    }
}
