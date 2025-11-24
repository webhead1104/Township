package me.webhead1104.towncraft;

import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface TowncraftPlayer {
    @NotNull
    UUID getUUID();

    @NotNull
    String getName();

    @NotNull
    TowncraftPlayerInventory getInventory();

    void openInventory(TowncraftInventory inventory);

    void closeInventory();

    TowncraftInventory getOpenInventory();

    void sendMessage(Component message);

    TowncraftItemStack getItemOnCursor();

    void setItemOnCursor(TowncraftItemStack itemStack);

    default User getUser() {
        return TowncraftPlatformManager.getUserManager().getUser(getUUID());
    }
}
