package me.webhead1104.towncraft.managers;

import lombok.NoArgsConstructor;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;


@NoArgsConstructor
public class InventoryManager {

    private final Map<UUID, Map<Integer, ItemStack>> playerInventories = new HashMap<>();

    public void addPlayerInventory(Player player) {
        Map<Integer, ItemStack> map = new HashMap<>();
        int i = 0;
        for (ItemStack itemStack : player.getInventory().getContents()) {
            map.put(i, Objects.requireNonNullElseGet(itemStack, () -> new ItemStack(Material.AIR)));
            i++;
        }
        playerInventories.put(player.getUniqueId(), map);
    }

    public void returnItemsToPlayer(Player player) {
        if (getPlayerInventory(player.getUniqueId()).isEmpty()) {
            player.sendMessage(Msg.format("<red>No items found!"));
            throw new NullPointerException("Player " + player.getUniqueId() + " does not have a inventory stored!");
        } else {
            int i = 0;
            for (ItemStack itemStack : getPlayerInventory(player.getUniqueId()).get().values()) {
                player.getInventory().setItem(i, itemStack);
                i++;
            }
            removePlayerInventory(player.getUniqueId());
        }
    }

    public @NotNull Optional<Map<Integer, ItemStack>> getPlayerInventory(UUID playerUUID) {
        return Optional.ofNullable(playerInventories.get(playerUUID));
    }

    public void removePlayerInventory(UUID playerUUID) {
        if (getPlayerInventory(playerUUID).isEmpty()) {
            throw new NullPointerException("Player " + playerUUID + " does not have a inventory stored!");
        } else playerInventories.remove(playerUUID);
    }
}
