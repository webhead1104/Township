package me.webhead1104.towncraft.events;

public interface TowncraftCancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
