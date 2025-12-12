package me.webhead1104.towncraft.impl.items;

import com.google.common.base.Preconditions;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftPlayerInventory;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public record TowncraftInventoryViewImpl(CytosisPlayer player,
                                         AbstractInventory topInventory,
                                         PlayerInventory bottomInventory) implements TowncraftInventoryView {
    @Override
    public TowncraftInventory getTopInventory() {
        return new TowncraftInventoryImpl(topInventory);
    }

    public TowncraftPlayerInventory getBottomInventory() {
        return new TowncraftPlayerInventoryImpl(bottomInventory);
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return topInventory.getViewers()
                .stream()
                .map(CytosisPlayer.class::cast)
                .map(TowncraftPlayerImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public int convertSlot(int rawSlot) {
        return rawSlot;
    }

    @Override
    public TowncraftPlayer getPlayer() {
        return new TowncraftPlayerImpl(player);
    }

    @Override
    public TowncraftItemStack getCursor() {
        return new TowncraftItemStackImpl(player.getInventory().getCursorItem());
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        TowncraftInventory inventory = getInventory(slot);
        if (inventory == null) {
            return null;
        }
        return new TowncraftItemStackImpl((ItemStack) inventory.getItem(slot).toPlatform());
    }

    @Override
    public void setItem(int slot, TowncraftItemStack item) {
        topInventory.setItemStack(slot, (ItemStack) item.toPlatform());
    }

    @Override
    public TowncraftInventory getInventory(int rawSlot) {
        if (rawSlot == -1) {
            return null;
        }
        Preconditions.checkArgument(rawSlot >= 0, "Negative, non outside slot %s", rawSlot);
        Preconditions.checkArgument(rawSlot < this.countSlots(), "Slot %s greater than inventory slot count", rawSlot);

        if (rawSlot > this.getTopInventory().getSize()) {
            return this.getTopInventory();
        } else {
            return getBottomInventory();
        }
    }

    private int countSlots() {
        return this.getTopInventory().getSize() + this.getBottomInventory().getSize();
    }
}
