package me.webhead1104.towncraft.impl.items;

import lombok.NonNull;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
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
    public Component getTitle() {
        return inventory.getType().defaultTitle();
    }

    @Override
    public void setTitle(Component title) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public String getTitleString() {
        return PlainTextComponentSerializer.plainText().serialize(inventory.getType().defaultTitle());
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
    public @Nullable TowncraftItemStack @NotNull [] getContents() {
        return Arrays.stream(inventory.getContents())
                .map(TowncraftItemStackImpl::new).toArray(TowncraftItemStackImpl[]::new);
    }

    @Override
    public void setContents(@Nullable TowncraftItemStack @NotNull [] items) {
        ItemStack[] itemStacks = new ItemStack[items.length];
        int i = 0;
        for (TowncraftItemStack item : items) {
            if (item == null) {
                itemStacks[i++] = null;
                continue;
            }
            itemStacks[i++] = (ItemStack) item.toPlatform();
        }
        inventory.setContents(itemStacks);
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
