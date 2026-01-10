/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
package me.webhead1104.towncraft.impl;

import com.google.common.base.Preconditions;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.items.TowncraftPlayerInventoryTestImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Slf4j
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TowncraftPlayerTestImpl implements TowncraftPlayer {
    @EqualsAndHashCode.Include
    private final UUID uuid;
    private final TowncraftPlayerInventory playerInventory;
    @Nullable
    private TowncraftInventory openInventory = null;
    private TowncraftItemStack itemOnCursor;

    public TowncraftPlayerTestImpl(@NotNull UUID uuid) {
        Preconditions.checkNotNull(uuid, "uuid cannot be null");
        this.uuid = uuid;
        this.playerInventory = new TowncraftPlayerInventoryTestImpl();
        this.itemOnCursor = TowncraftItemStack.empty();
    }

    @Override
    public @NotNull UUID getUUID() {
        return uuid;
    }

    @Override
    public @NotNull String getName() {
        return "Webhead1104";
    }

    @Override
    public @NotNull TowncraftPlayerInventory getInventory() {
        return playerInventory;
    }

    @Override
    public void openInventory(@NotNull TowncraftInventory inventory) {
        Preconditions.checkNotNull(inventory, "inventory cannot be null");
        openInventory = inventory;
        openInventory.getViewers().add(this);
    }

    @Override
    public void closeInventory() {
        if (openInventory != null) {
            openInventory.getViewers().remove(this);
        }
        openInventory = null;
    }

    @Override
    public @Nullable TowncraftInventory getOpenInventory() {
        return openInventory;
    }

    @Override
    public void sendMessage(@NotNull Component message) {
        Preconditions.checkNotNull(message, "message cannot be null");
        log.info("Sent message: {}", MiniMessage.miniMessage().serialize(message));
    }

    @Override
    public TowncraftItemStack getItemOnCursor() {
        return itemOnCursor;
    }

    @Override
    public void setItemOnCursor(@NotNull TowncraftItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "itemStack cannot be null");
        itemOnCursor = itemStack;
    }
}
