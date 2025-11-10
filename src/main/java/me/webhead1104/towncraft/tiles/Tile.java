package me.webhead1104.towncraft.tiles;

import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.data.objects.WorldSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public abstract class Tile {
    public abstract ItemStack render(SlotRenderContext context);

    public abstract boolean onClick(SlotClickContext context);

    @ApiStatus.OverrideOnly
    public boolean onUpdate(SlotContext slotContext, WorldSection worldSection, int slot) {
        return false;
    }

    @ApiStatus.OverrideOnly
    public void onLoad(WorldSection worldSection, int slot) {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return obj != null && getClass() == obj.getClass();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}