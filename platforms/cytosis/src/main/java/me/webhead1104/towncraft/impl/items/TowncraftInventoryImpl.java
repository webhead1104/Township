package me.webhead1104.towncraft.impl.items;

import lombok.NonNull;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.impl.TowncraftPlayerImpl;
import me.webhead1104.towncraft.items.TowncraftInventory;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.TowncraftInventoryType;
import net.cytonic.cytosis.player.CytosisPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TowncraftInventoryImpl implements TowncraftInventory {
    private final AbstractInventory inventory;

    public TowncraftInventoryImpl(AbstractInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public List<TowncraftPlayer> getViewers() {
        return inventory.getViewers().stream()
                .map(CytosisPlayer.class::cast)
                .map(TowncraftPlayerImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public int getSize() {
        return inventory.getSize();
    }

    @Override
    public Component getTitle() {
        if (inventory instanceof PlayerInventory) {
            throw new UnsupportedOperationException("This operation is not supported.");
        }

        return ((Inventory) inventory).getTitle();
    }

    @Override
    public void setTitle(Component title) {
        if (inventory instanceof PlayerInventory) {
            throw new UnsupportedOperationException("This operation is not supported.");
        }

        ((Inventory) inventory).setTitle(title);
    }

    @Override
    public void setTitle(String title) {
        if (inventory instanceof PlayerInventory) {
            throw new UnsupportedOperationException("This operation is not supported.");
        }

        ((Inventory) inventory).setTitle(Component.text(title));
    }

    @Override
    public String getTitleString() {
        if (inventory instanceof PlayerInventory) {
            throw new UnsupportedOperationException("This operation is not supported.");
        }

        return PlainTextComponentSerializer.plainText().serialize(((Inventory) inventory).getTitle());
    }

    @Override
    public TowncraftInventoryType getType() {
        if (inventory instanceof PlayerInventory) {
            return TowncraftInventoryType.PLAYER;
        }
        return switch (((Inventory) inventory).getInventoryType()) {
            case CHEST_1_ROW, CHEST_2_ROW, CHEST_3_ROW, CHEST_4_ROW, CHEST_5_ROW, CHEST_6_ROW ->
                    TowncraftInventoryType.CHEST;
            default -> TowncraftInventoryType.valueOf(((Inventory) inventory).getInventoryType().name());
        };
    }

    @Override
    public void setItem(int slot, @NonNull TowncraftItemStack itemStack) {
        inventory.setItemStack(slot, (ItemStack) itemStack.toPlatform());
    }

    @Override
    public TowncraftItemStack getItem(int slot) {
        return new TowncraftItemStackImpl(inventory.getItemStack(slot));
    }

    @Override
    public @Nullable TowncraftItemStack @NotNull [] getContents() {
        return Arrays.stream(inventory.getItemStacks())
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
        inventory.copyContents(itemStacks);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public void clear(int slot) {
        inventory.setItemStack(slot, ItemStack.AIR);
    }

    @Override
    public Object getPlatform() {
        return inventory;
    }
}
