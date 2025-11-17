package me.webhead1104.towncraft.managers;

import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class InventoryManager {
    private final Map<UUID, @Nullable ItemStack @NotNull []> playerInventories = new HashMap<>();

    public void addPlayerInventory(Player player) {
        playerInventories.put(player.getUniqueId(), player.getInventory().getContents());
    }

    public void returnItemsToPlayer(Player player) {
        if (getPlayerInventory(player.getUniqueId()).isEmpty()) {
            player.sendMessage(Msg.format("<red>No items found!"));
            throw new NullPointerException("Player " + player.getUniqueId() + " does not have a inventory stored!");
        } else {
            player.getInventory().setContents(getPlayerInventory(player.getUniqueId()).get());
            removePlayerInventory(player.getUniqueId());
        }
    }

    public @NotNull Optional<@Nullable ItemStack @NotNull []> getPlayerInventory(UUID playerUUID) {
        return Optional.ofNullable(playerInventories.get(playerUUID));
    }

    public void removePlayerInventory(UUID playerUUID) {
        if (getPlayerInventory(playerUUID).isEmpty()) {
            throw new NullPointerException("Player " + playerUUID + " does not have a inventory stored!");
        } else playerInventories.remove(playerUUID);
    }
}
