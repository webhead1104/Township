package me.webhead1104.towncraft.tiles;

import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.context.SlotContext;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import org.jetbrains.annotations.ApiStatus;

public abstract class Tile {
    public abstract TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot);

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