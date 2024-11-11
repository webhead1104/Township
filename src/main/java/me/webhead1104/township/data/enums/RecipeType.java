package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum RecipeType {
    NONE(MenuItems.none, ItemType.NONE, Map.of(), 0, "none_recipe"),
    BREAD(MenuItems.Recipes.bread, ItemType.BREAD, Map.of(ItemType.WHEAT, 2), 138, "bread_recipe"),
    COOKIE(MenuItems.Recipes.cookie, ItemType.COOKIE, Map.of(ItemType.WHEAT, 2, ItemType.EGG, 2), 438, "cookie_recipe"),
    BAGEL(MenuItems.Recipes.bagel, ItemType.BAGEL, Map.of(ItemType.WHEAT, 2, ItemType.EGG, 3, ItemType.SUGAR, 1), 900, "bagel_recipe"),
    COW_FEED(MenuItems.Recipes.cowFeed, ItemType.COW_FEED, Map.of(ItemType.WHEAT, 2, ItemType.CORN, 1), 138, "cow_feed_recipe"),
    CHICKEN_FEED(MenuItems.Recipes.chickenFeed, ItemType.CHICKEN_FEED, Map.of(ItemType.WHEAT, 2, ItemType.CARROT, 1), 300, "chicken_feed_recipe"),
    CREAM(MenuItems.Recipes.cream, ItemType.CREAM, Map.of(ItemType.MILK, 1), 438, "cream_recipe"),
    CHEESE(MenuItems.Recipes.cheese, ItemType.CHEESE, Map.of(ItemType.MILK, 2), 900, "cheese_recipe"),
    BUTTER(MenuItems.Recipes.butter, ItemType.BUTTER, Map.of(ItemType.MILK, 3), 1800, "butter_recipe"),
    YOGURT(MenuItems.Recipes.yogurt, ItemType.YOGURT, Map.of(ItemType.MILK, 4), 2700, "yogurt_recipe"),
    SUGAR(MenuItems.Recipes.sugar, ItemType.SUGAR, Map.of(ItemType.SUGARCANE, 1), 600, "sugar_recipe"),
    SYRUP(MenuItems.Recipes.syrup, ItemType.SYRUP, Map.of(ItemType.SUGARCANE, 2), 1200, "syrup_recipe"),
    CARAMEL(MenuItems.Recipes.caramel, ItemType.CARAMEL, Map.of(ItemType.SUGARCANE, 3), 2700, "caramel_recipe");

    private final ItemStack menuItem;
    private final ItemType itemType;
    private final Map<ItemType, Integer> recipeItems;
    private final int time;
    private final String id;
}
