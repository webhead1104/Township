package me.webhead1104.township.tiles;

import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import org.bukkit.inventory.ItemStack;

public abstract class Tile {
    public abstract ItemStack render(SlotRenderContext context);

    public abstract boolean onClick(SlotClickContext context);
}