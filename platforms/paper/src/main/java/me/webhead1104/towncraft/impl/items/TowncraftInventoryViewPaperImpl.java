package me.webhead1104.towncraft.impl.items;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerPaperImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftInventoryView;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.stream.Collectors;

public record TowncraftInventoryViewPaperImpl(InventoryView view) implements TowncraftInventoryView {
    @Override
    public TowncraftInventory getTopInventory() {
        return new TowncraftInventoryPaperImpl(view.getTopInventory());
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return view.getTopInventory().getViewers()
                .stream().map(TowncraftPlayerPaperImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public int convertSlot(int slot) {
        return view.convertSlot(slot);
    }

    @Override
    public TowncraftPlayer getPlayer() {
        return new TowncraftPlayerPaperImpl(view.getPlayer());
    }

    @Override
    public TowncraftItemStack getCursor() {
        return new TowncraftItemStackPaperImpl(view.getCursor());
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        return new TowncraftItemStackPaperImpl(view.getItem(slot));
    }

    @Override
    public void setItem(int slot, TowncraftItemStack item) {
        view.setItem(slot, (ItemStack) item.toPlatform());
    }

    @Override
    public TowncraftInventory getInventory(int slot) {
        if (view.getInventory(slot) instanceof PlayerInventory playerInventory) {
            return new TowncraftPlayerInventoryPaperImpl(playerInventory);
        }
        return new TowncraftInventoryPaperImpl(view.getInventory(slot));
    }
}
