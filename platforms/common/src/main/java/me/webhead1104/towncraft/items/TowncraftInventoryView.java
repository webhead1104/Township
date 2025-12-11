package me.webhead1104.towncraft.items;

import me.webhead1104.towncraft.TowncraftPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TowncraftInventoryView {
    TowncraftInventory getTopInventory();

    List<TowncraftPlayer> getViewers();

    int convertSlot(int slot);

    TowncraftPlayer getPlayer();

    TowncraftItemStack getCursor();

    @Nullable
    TowncraftItemStack getItem(int slot);

    void setItem(int slot, TowncraftItemStack item);

    @Nullable
    TowncraftInventory getInventory(int slot);
}
