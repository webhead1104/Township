package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.data.enums.HouseType;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class HouseTile extends Tile {
    private final HouseType houseType;

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(houseType.getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        return false;
    }
}
