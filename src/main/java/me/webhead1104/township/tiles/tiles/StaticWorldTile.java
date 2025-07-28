package me.webhead1104.township.tiles.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.tiles.Tile;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class StaticWorldTile extends Tile {
    private final Material material;

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = new ItemStack(material);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Component.empty());
        itemStack.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(true));
        return itemStack;
    }

    @Override
    public void onClick(SlotClickContext context) {
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        GRASS(new StaticWorldTile(Material.GRASS_BLOCK)),
        STONE(new StaticWorldTile(Material.STONE)),
        SAND(new StaticWorldTile(Material.SAND)),
        WATER(new StaticWorldTile(Material.COBBLESTONE)),
        ROAD(new StaticWorldTile(Material.BLACK_CONCRETE)),
        PLANKS(new StaticWorldTile(Material.OAK_PLANKS));
        private final StaticWorldTile tile;
    }
}
