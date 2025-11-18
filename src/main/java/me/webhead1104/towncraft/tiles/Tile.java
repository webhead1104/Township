package me.webhead1104.towncraft.tiles;

import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.data.objects.WorldSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

public abstract class Tile {
    public abstract ItemStack render(SlotRenderContext context, WorldSection worldSection, int slot);

    public abstract boolean onClick(SlotClickContext context, WorldSection worldSection, int slot);

    @ApiStatus.OverrideOnly
    public void onUpdate(SlotContext slotContext, WorldSection worldSection, int slot) {
        if (this instanceof TimeFinishable timeFinishable) {
            timeFinishable.handleUpdate(slotContext, worldSection, slot);
        }
    }

    @ApiStatus.OverrideOnly
    public void onLoad(RenderContext renderContext, WorldSection worldSection, int slot) {
        if (this instanceof TimeFinishable timeFinishable) {
            timeFinishable.handleUpdate(renderContext, worldSection, slot);
        }
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