package me.webhead1104.towncraft;

public interface Scheduler {
    void runTaskNextTick(Runnable runnable);

    TowncraftTask runTimer(Runnable runnable, long delay, long period);
}
