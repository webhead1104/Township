package me.webhead1104.towncraft.items;

import me.webhead1104.towncraft.TowncraftPlayer;

import java.util.List;

public interface TowncraftInventoryView {
    TowncraftInventory getTopInventory();

    List<TowncraftPlayer> getViewers();

    int convertSlot(int slot);

    TowncraftPlayer getPlayer();

    TowncraftItemStack getCursor();

    TowncraftItemStack getItem(int slot);

    void setItem(int slot, TowncraftItemStack item);

    TowncraftInventory getInventory(int slot);
}
