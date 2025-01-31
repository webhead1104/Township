package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum RecipeType {
    NONE(MenuItems.none, ItemType.NONE, Map.of(), -1, -1, -1, "none_recipe"),
    BREAD(MenuItems.Recipes.bread, ItemType.BREAD, Map.of(ItemType.WHEAT, 2), 300, 2, 2, "bread_recipe"),
    COOKIE(MenuItems.Recipes.cookie, ItemType.COOKIE, Map.of(ItemType.WHEAT, 2, ItemType.EGG, 2), 438, 5, 19, "cookie_recipe"),
    BAGEL(MenuItems.Recipes.bagel, ItemType.BAGEL, Map.of(ItemType.WHEAT, 2, ItemType.EGG, 3, ItemType.SUGAR, 1), 900, 8, 24, "bagel_recipe"),
    COW_FEED(MenuItems.Recipes.cowFeed, ItemType.COW_FEED, Map.of(ItemType.WHEAT, 2, ItemType.CORN, 1), 138, 1, 1, "cow_feed_recipe"),
    CHICKEN_FEED(MenuItems.Recipes.chickenFeed, ItemType.CHICKEN_FEED, Map.of(ItemType.WHEAT, 2, ItemType.CARROT, 1), 300, 5, 2, "chicken_feed_recipe"),
    CREAM(MenuItems.Recipes.cream, ItemType.CREAM, Map.of(ItemType.MILK, 1), 438, 4, 5, "cream_recipe"),
    CHEESE(MenuItems.Recipes.cheese, ItemType.CHEESE, Map.of(ItemType.MILK, 2), 900, 6, 11, "cheese_recipe"),
    SUGAR(MenuItems.Recipes.sugar, ItemType.SUGAR, Map.of(ItemType.SUGARCANE, 1), 600, 7, 6, "sugar_recipe");

    private final ItemStack menuItem;
    private final ItemType itemType;
    private final Map<ItemType, Integer> recipeItems;
    //in seconds
    private final int time;
    private final int levelNeeded;
    private final int xpGiven;
    private final String id;
}
