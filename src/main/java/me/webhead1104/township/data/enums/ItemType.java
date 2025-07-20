package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static me.webhead1104.township.utils.Utils.getItemStackItem;

@Getter
@AllArgsConstructor
public enum ItemType {
    NONE(getItemStackItem("None Item", Material.BARRIER), -1, "none"),
    WHEAT(getItemStackItem("Wheat", Material.WHEAT), 1, "wheat"),
    CORN(getItemStackItem("Corn", Material.PLAYER_HEAD), 3, "corn"),
    CARROT(getItemStackItem("Carrot", Material.CARROT), 5, "carrot"),
    SUGARCANE(getItemStackItem("Sugar Cane", Material.SUGAR_CANE), 7, "sugarcane"),
    BREAD(getItemStackItem("Bread", Material.BREAD), 5, "bread"),
    COOKIE(getItemStackItem("Cookie", Material.COOKIE), 44, "cookie"),
    BAGEL(getItemStackItem("Bagel", Material.PLAYER_HEAD), 55, "bagel"),
    CREAM(getItemStackItem("Cream", Material.PLAYER_HEAD), 12, "cream"),
    CHEESE(getItemStackItem("Cheese", Material.PLAYER_HEAD), 25, "cheese"),
    SUGAR(getItemStackItem("Sugar", Material.SUGAR), 14, "sugar"),
    MILK(getItemStackItem("Milk", Material.MILK_BUCKET), 7, "milk"),
    EGG(getItemStackItem("Egg", Material.EGG), 10, "egg"),
    PAINT(getItemStackItem("Paint", Material.PLAYER_HEAD), 100, "paint"),
    HAMMER(getItemStackItem("Hammer", Material.PLAYER_HEAD), 100, "hammer"),
    NAIL(getItemStackItem("Nail", Material.PLAYER_HEAD), 100, "nail"),
    COW_FEED(getItemStackItem("Cow Feed", Material.PLAYER_HEAD), 1, "cow_feed"),
    CHICKEN_FEED(getItemStackItem("Chicken Feed", Material.PLAYER_HEAD), 4, "chicken_feed");
    private final ItemStack itemStack;
    private final int sellPrice;
    private final String ID;
}