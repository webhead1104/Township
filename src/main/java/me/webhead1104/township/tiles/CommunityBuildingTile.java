package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import lombok.Setter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.features.world.build.CommunityBuildingType;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class CommunityBuildingTile extends BuildingTile {
    private final CommunityBuildingType communityBuildingType;

    public CommunityBuildingTile(int buildingSlot, CommunityBuildingType communityBuildingType) {
        super(communityBuildingType.getBuildingType(), buildingSlot);
        this.communityBuildingType = communityBuildingType;
    }

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(communityBuildingType.getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        return false;
    }
}
