package me.webhead1104.township.tiles;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.devnatan.inventoryframework.context.SlotClickContext;
import me.devnatan.inventoryframework.context.SlotRenderContext;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.BuildingType;
import me.webhead1104.township.menus.TrainMenu;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;

public class TrainTile extends BuildingTile {
    public TrainTile() {
        super(BuildingType.TRAIN, 0);
    }

    @Override
    public ItemStack render(SlotRenderContext context) {
        ItemStack itemStack = ItemStack.of(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Trains"));
        String textures = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmQ1YjBlM2FhNjExNjJhM2M2NTM4OTg1YzVjMTFjZWI5NmQ1NjA1YjNjZTkyMzRjODhmNGZiZDcyMzQ3NWQifX19";
        itemStack.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile().addProperty(new ProfileProperty("textures", textures)));
        itemStack.setData(DataComponentTypes.RARITY, ItemRarity.COMMON);
        return itemStack;
    }

    @Override
    public boolean onClick(SlotClickContext context) {
        if (Township.getUserManager().getUser(context.getPlayer().getUniqueId()).getTrains().isUnlocked()) {
            context.openForPlayer(TrainMenu.class);
            return true;
        }
        return false;
    }
}
