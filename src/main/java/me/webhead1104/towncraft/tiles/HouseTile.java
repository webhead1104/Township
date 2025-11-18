package me.webhead1104.towncraft.tiles;

import com.google.errorprone.annotations.Keep;
import io.papermc.paper.datacomponent.DataComponentTypes;
import lombok.Getter;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.world.build.HouseType;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public class HouseTile extends BuildingTile {
    private final HouseType houseType;

    @Keep
    protected HouseTile(HouseType houseType) {
        super(houseType.getBuildingType());
        this.houseType = houseType;
    }

    @Override
    public ItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(houseType.getName()));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        return false;
    }
}
