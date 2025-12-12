package me.webhead1104.towncraft;

import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.utils.Msg;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InventoryManager {
    private final Map<UUID, @Nullable TowncraftItemStack @NotNull []> playerInventories = new HashMap<>();
    private boolean enabled = false;

    public void enable() {
        enabled = true;
    }

    public void addPlayerInventory(TowncraftPlayer player) {
        if (!enabled) return;
        playerInventories.put(player.getUUID(), player.getInventory().getContents());
    }

    public void returnItemsToPlayer(TowncraftPlayer player) {
        if (!enabled) return;
        if (getPlayerInventory(player.getUUID()).isEmpty()) {
            player.sendMessage(Msg.format("<red>No items found!"));
            throw new NullPointerException("Player " + player.getUUID() + " does not have a inventory stored!");
        } else {
            player.getInventory().setContents(getPlayerInventory(player.getUUID()).get());
            removePlayerInventory(player.getUUID());
        }
    }

    public @NotNull Optional<@Nullable TowncraftItemStack @NotNull []> getPlayerInventory(UUID playerUUID) {
        if (!enabled) return Optional.empty();
        return Optional.ofNullable(playerInventories.get(playerUUID));
    }

    public void removePlayerInventory(UUID playerUUID) {
        if (!enabled) return;
        if (getPlayerInventory(playerUUID).isEmpty()) {
            throw new NullPointerException("Player " + playerUUID + " does not have a inventory stored!");
        } else playerInventories.remove(playerUUID);
    }
}
