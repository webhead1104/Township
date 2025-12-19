package me.webhead1104.towncraft.impl;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.items.TowncraftInventoryPaperImpl;
import me.webhead1104.towncraft.impl.items.TowncraftItemStackPaperImpl;
import me.webhead1104.towncraft.impl.items.TowncraftPlayerInventoryPaperImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record TowncraftPlayerPaperImpl(Player player) implements TowncraftPlayer {

    public TowncraftPlayerPaperImpl(HumanEntity humanEntity) {
        this((Player) humanEntity);
    }

    @Override
    public @NotNull UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return player.getName();
    }

    @Override
    public @NotNull TowncraftPlayerInventory getInventory() {
        return new TowncraftPlayerInventoryPaperImpl(player.getInventory());
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
        return new TowncraftInventoryPaperImpl((Inventory) player.getOpenInventory());
    }

    @Override
    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    @Override
    public TowncraftItemStack getItemOnCursor() {
        return new TowncraftItemStackPaperImpl(player.getItemOnCursor());
    }

    @Override
    public void setItemOnCursor(TowncraftItemStack itemStack) {
        player.setItemOnCursor((ItemStack) itemStack.toPlatform());
    }
}
