package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.menus.ExpansionMenu;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
@AllArgsConstructor
public class ExpansionTile extends Tile {
    private Expansion expansion;

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = ItemStack.of(Material.PODZOL);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Expansion"));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        context.openForPlayer(ExpansionMenu.class, expansion);
        return true;
    }
}
