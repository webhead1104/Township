package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.build.CommunityBuildingType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;

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
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format(communityBuildingType.getName()));
        return itemStack;
    }

}
