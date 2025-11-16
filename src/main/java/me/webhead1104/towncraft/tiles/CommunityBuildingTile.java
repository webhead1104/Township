package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import lombok.Setter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.features.world.build.CommunityBuildingType;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class CommunityBuildingTile extends BuildingTile {
    private final CommunityBuildingType communityBuildingType;

    @Keep
    public CommunityBuildingTile(CommunityBuildingType communityBuildingType) {
        super(communityBuildingType.getBuildingType());
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
