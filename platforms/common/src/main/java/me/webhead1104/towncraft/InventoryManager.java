/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
