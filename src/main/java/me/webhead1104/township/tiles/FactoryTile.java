package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.features.factories.FactoryMenu;
import me.webhead1104.township.features.factories.FactoryType;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class FactoryTile extends BuildingTile {
    private final Key factoryType;

    public FactoryTile(int buildingSlot, Key factoryType) {
        super(buildingSlot);
        this.factoryType = factoryType;
    }

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(FactoryType.getFactory(factoryType).getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        context.openForPlayer(FactoryMenu.class, factoryType);
        return true;
    }
}
