package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.features.world.build.HouseType;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class HouseTile extends BuildingTile {
    private final HouseType houseType;

    public HouseTile(int buildingSlot, HouseType houseType) {
        super(houseType.getBuildingType(), buildingSlot);
        this.houseType = houseType;
    }

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
