package me.webhead1104.towncraft.menus.internal;

import me.devnatan.inventoryframework.internal.Job;
import me.webhead1104.towncraft.Scheduler;
import me.webhead1104.towncraft.TowncraftTask;

class TowncraftTaskJobImpl implements Job {

    private final Scheduler scheduler;
    private final long intervalInTicks;
    private final Runnable execution;
    private TowncraftTask task;

    public TowncraftTaskJobImpl(Scheduler scheduler, long intervalInTicks, Runnable execution) {
        this.scheduler = scheduler;
        this.intervalInTicks = intervalInTicks;
        this.execution = execution;
    }

    @Override
    public boolean isStarted() {
        return task != null && !task.isCancelled();
    }

    @Override
    public void start() {
        if (isStarted()) return;

        task = scheduler.runTimer(this::loop, intervalInTicks, intervalInTicks);
    }

    @Override
    public void cancel() {
        if (!isStarted()) return;
        task.cancel();
        task = null;
    }

    @Override
    public void loop() {
        execution.run();
    }
}
