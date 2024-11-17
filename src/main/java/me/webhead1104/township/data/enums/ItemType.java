package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public enum ItemType {
    NONE(MenuItems.none, -1, "none"),
    WHEAT(MenuItems.wheat, 1, "wheat"),
    CORN(MenuItems.corn, 3, "corn"),
    CARROT(MenuItems.carrot, 5, "carrot"),
    SUGARCANE(MenuItems.sugarcane, 7, "sugarcane"),
    BREAD(MenuItems.bread, 5, "bread"),
    COOKIE(MenuItems.cookie, 44, "cookie"),
    BAGEL(MenuItems.bagel, 55, "bagel"),
    COW_FEED(MenuItems.cowFeed, 1, "cow_feed"),
    CHICKEN_FEED(MenuItems.chickenFeed, 4, "chicken_feed"),
    CREAM(MenuItems.cream, 12, "cream"),
    CHEESE(MenuItems.cheese, 25, "cheese"),
    SUGAR(MenuItems.sugar, 14, "sugar"),
    MILK(MenuItems.milk, 7, "milk"),
    EGG(MenuItems.egg, 10, "egg"),
    PAINT(MenuItems.paint, 100, "paint"),
    HAMMER(MenuItems.hammer, 100, "hammer"),
    NAIL(MenuItems.nail, 100, "nail");

    private final ItemStack itemStack;
    private final int sellPrice;
    private final String ID;
}