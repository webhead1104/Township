package me.webhead1104.township.tiles.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.PlotType;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.menus.PlotMenu;
import me.webhead1104.township.tiles.Tile;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PlotTile extends Tile {
    private Plot plot;

    @Override
    public ItemStack render(SlotRenderContext context) {
        if (!plot.getInstant().equals(Instant.EPOCH) && Instant.now().isAfter(plot.getInstant().minusSeconds(1))) {
            plot.setClaimable(true);
            plot.setInstant(Instant.EPOCH);
        }

        if (plot.getPlotType().equals(PlotType.NONE) || plot.getInstant().equals(Instant.EPOCH)) {
            return plot.getPlotType().getMenuItem();
        }

        ItemStack itemStack = plot.getPlotType().getMenuItem();
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<gold>Time: %s", Utils.format(Instant.now(), plot.getInstant())))));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        if (plot.getPlotType().equals(PlotType.NONE)) {
            context.openForPlayer(PlotMenu.class, plot);
            return true;
        } else if (plot.isClaimable()) {
            plot.setClaimable(false);
            Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getBarn().addAmountToItem(plot.getPlotType().getItemType(), 1);
            plot.setPlotType(PlotType.NONE);
        }
        return false;
    }
}
