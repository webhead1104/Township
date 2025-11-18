package me.webhead1104.towncraft.tiles;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.WorldSection;
import me.webhead1104.towncraft.features.barn.BarnMenu;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BarnTile extends BuildingTile {
    public BarnTile() {
        super(Towncraft.key("barn"));
    }

    @Override
    public ItemStack render(SlotRenderContext context, WorldSection worldSection, int slot) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("<blue>Barn"));
        String textures =
                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE5OTUzY2U5NDVkYTZlMmZiODAwMzBlNzU0ZmE4ZTA5MmM0ZTllY2QwNTQ4ZjkzOGMzNDk3YTgwOGU0MWE0OCJ9fX0=";
        itemStack.setData(DataComponentTypes.PROFILE,
                ResolvableProfile.resolvableProfile().addProperty(new ProfileProperty("textures", textures)));
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context, WorldSection worldSection, int slot) {
        context.openForPlayer(BarnMenu.class);
        return true;
    }
}
