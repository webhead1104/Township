package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.menus.FactoryMenu;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class FactoryTile extends Tile {
    private final FactoryType factoryType;

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(factoryType.getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        if (Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getFactories().getFactory(factoryType).isUnlocked()) {
            context.openForPlayer(FactoryMenu.class, factoryType);
            return true;
        }
        return false;
    }
}
