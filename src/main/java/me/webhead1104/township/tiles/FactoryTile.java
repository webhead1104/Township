package me.webhead1104.township.tiles;

import com.google.errorprone.annotations.Keep;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.features.factories.FactoryMenu;
import me.webhead1104.township.features.factories.FactoryType;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class FactoryTile extends BuildingTile {
    private final Key factoryType;

    @Keep
    public FactoryTile(Key factoryType) {
        super(Township.getDataLoader(FactoryType.class).get(factoryType).getBuildingKey(), 0);
        this.factoryType = factoryType;
    }

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(Township.getDataLoader(FactoryType.class).get(factoryType).getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        context.openForPlayer(FactoryMenu.class, factoryType);
        return true;
    }
}
