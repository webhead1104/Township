package me.webhead1104.towncraft.impl.items;

import lombok.NonNull;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class TowncraftInventoryImpl implements TowncraftInventory {
    private final Inventory inventory;

    public TowncraftInventoryImpl(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return inventory.getViewers().stream()
                .map(TowncraftPlayerImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public String getTitle() {
        return PlainTextComponentSerializer.plainText().serialize(inventory.getType().defaultTitle());
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public TowncraftInventoryType getType() {
        return TowncraftInventoryType.valueOf(inventory.getType().name());
    }

    @Override
    public void setItem(int slot, @NonNull TowncraftItemStack itemStack) {
        inventory.setItem(slot, (ItemStack) itemStack.toPlatform());
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        return new TowncraftItemStackImpl(inventory.getItem(slot));
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void clear(int slot) {
        inventory.clear(slot);
    }

    @Override
    public Object getPlatform() {
        return inventory;
    }
}
