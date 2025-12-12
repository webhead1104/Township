package me.webhead1104.towncraft.items;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface TowncraftInventory {
    List<TowncraftPlayer> getViewers();

    int getSize();

    Component getTitle();

    void setTitle(Component title);

    void setTitle(String title);

    String getTitleString();

    TowncraftInventoryType getType();

    void setItem(int slot, TowncraftItemStack itemStack);

    TowncraftItemStack getItem(int slot);

    @Nullable TowncraftItemStack @NotNull [] getContents();

    void setContents(@Nullable TowncraftItemStack @NotNull [] items);

    void clear();

    void clear(int slot);

    Object getPlatform();
}
