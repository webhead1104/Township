package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.data.objects.Barn;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public enum RecipeType {
    NONE(Material.BARRIER, ItemType.NONE, Map.of(), -1, -1, -1),
    BREAD(Material.BREAD, ItemType.BREAD, Map.of(ItemType.WHEAT, 2), 10, 2, 2),
    COOKIE(Material.COOKIE, ItemType.COOKIE, Map.of(ItemType.WHEAT, 2, ItemType.EGG, 2), 10, 5, 19),
    BAGEL(Material.PLAYER_HEAD, ItemType.BAGEL, Map.of(ItemType.WHEAT, 2, ItemType.EGG, 3, ItemType.SUGAR, 1), 900, 8, 24),
    COW_FEED(Material.PLAYER_HEAD, ItemType.COW_FEED, Map.of(ItemType.WHEAT, 2, ItemType.CORN, 1), 138, 1, 1),
    CHICKEN_FEED(Material.PLAYER_HEAD, ItemType.CHICKEN_FEED, Map.of(ItemType.WHEAT, 2, ItemType.CARROT, 1), 300, 5, 2),
    CREAM(Material.PLAYER_HEAD, ItemType.CREAM, Map.of(ItemType.MILK, 1), 438, 4, 5),
    CHEESE(Material.PLAYER_HEAD, ItemType.CHEESE, Map.of(ItemType.MILK, 2), 900, 6, 11),
    SUGAR(Material.SUGAR, ItemType.SUGAR, Map.of(ItemType.SUGARCANE, 1), 600, 7, 6);
    private final ItemStack itemStack;
    private final ItemType resultItemType;
    private final Map<ItemType, Integer> recipeItems;
    //in seconds
    private final int time;
    private final int levelNeeded;
    private final int xpGiven;

    RecipeType(Material material, ItemType resultItemType, Map<ItemType, Integer> recipeItems, int time, int levelNeeded, int xpGiven) {
        this.itemStack = Utils.getItemStack(Utils.thing2(name()), material);
        this.resultItemType = resultItemType;
        this.recipeItems = recipeItems;
        this.time = time;
        this.levelNeeded = levelNeeded;
        this.xpGiven = xpGiven;
    }

    public boolean hasRequiredItems(Barn barn) {
        for (Map.Entry<ItemType, Integer> entry : recipeItems.entrySet()) {
            if (barn.getItem(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }
}
