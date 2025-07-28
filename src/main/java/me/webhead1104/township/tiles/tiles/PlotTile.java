package me.webhead1104.township.tiles.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.tiles.Tile;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@AllArgsConstructor
public class PlotTile extends Tile {
    private Plot plot;

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = ItemStack.of(Material.FARMLAND);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Empty Plot"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>You should plant something!"))));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        ItemStack cursor = context.getPlayer().getItemOnCursor();
        if (cursor.isEmpty()) {
            Township.getPlotManager().openMenu(context.getPlayer());
        } else {
            Township.getPlotManager().plant(context.getPlayer(), context.getItem(), cursor);
        }
        return true;
    }
}
