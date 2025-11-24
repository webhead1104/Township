package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import lombok.Getter;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.build.HouseType;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import me.webhead1104.towncraft.menus.context.SlotClickContext;
import me.webhead1104.towncraft.menus.context.SlotRenderContext;
import me.webhead1104.towncraft.utils.Msg;

@Getter
public class HouseTile extends BuildingTile {
    private final HouseType houseType;

    @Keep
    protected HouseTile(HouseType houseType) {
        super(houseType.getBuildingType());
        this.houseType = houseType;
    }

    @Override
    public TowncraftItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(TowncraftMaterial.PLAYER_HEAD);
        itemStack.setName(Msg.format(houseType.getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        return false;
    }
}
