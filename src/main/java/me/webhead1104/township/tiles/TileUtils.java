package me.webhead1104.township.tiles;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.experimental.UtilityClass;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@UtilityClass
public class TileUtils {

    public static ItemStack createPurchableItemStack(String realName, boolean showNeedToPurchase) {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format(realName));
        if (showNeedToPurchase) {
            itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<red>You need to purchase the %s!", realName))));
        }
        return itemStack;
    }
}
