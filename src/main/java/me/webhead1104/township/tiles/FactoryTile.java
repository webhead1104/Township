package me.webhead1104.township.tiles;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.objects.Factories;
import me.webhead1104.township.menus.FactoryMenu;
import me.webhead1104.township.tiles.TileUtils;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public class FactoryTile extends Tile {
    private final FactoryType factoryType;

    @Override
    public ItemStack render(SlotRenderContext context) {
        Factories.Factory factory = Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getFactories().getFactory(factoryType);
        return TileUtils.createPurchableItemStack(factoryType.getName(), !factory.isUnlocked());
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
