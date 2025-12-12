package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryImpl;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackImpl;
import me.webhead1104.towncraft.impl.items.TowncraftPlayerInventoryImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record TowncraftPlayerImpl(CytosisPlayer player) implements TowncraftPlayer {

    @Override
    public @NotNull UUID getUUID() {
        return player.getUuid();
    }

    @Override
    public @NotNull String getName() {
        return player.getUsername();
    }

    @Override
    public @NotNull TowncraftPlayerInventory getInventory() {
        return new TowncraftPlayerInventoryImpl(player.getInventory());
    }

    @Override
    public void openInventory(TowncraftInventory inventory) {
        player.openInventory((Inventory) inventory.getPlatform());
    }

    @Override
    public void closeInventory() {
        player.closeInventory();
    }

    @Override
    public TowncraftInventory getOpenInventory() {
        return new TowncraftInventoryImpl(player.getOpenInventory());
    }

    @Override
    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    @Override
    public TowncraftItemStack getItemOnCursor() {
        return new TowncraftItemStackImpl(player.getInventory().getCursorItem());
    }

    @Override
    public void setItemOnCursor(TowncraftItemStack itemStack) {
        player.getInventory().setCursorItem((ItemStack) itemStack.toPlatform());
    }
}
