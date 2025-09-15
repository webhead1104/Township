package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum ItemType {
    NONE(Material.BARRIER, -1, -1),
    WHEAT(Material.WHEAT, 1, 1),
    CORN(Material.PLAYER_HEAD, 3, 1),
    CARROT(Material.CARROT, 5, 2),
    SUGARCANE(Material.SUGAR_CANE, 7, 3),
    BREAD(Material.BREAD, 5, 2),
    COOKIE(Material.COOKIE, 44, 19),
    BAGEL(Material.PLAYER_HEAD, 55, 24),
    CREAM(Material.PLAYER_HEAD, 12, 5),
    CHEESE(Material.PLAYER_HEAD, 25, 11),
    SUGAR(Material.SUGAR, 14, 6),
    MILK(Material.MILK_BUCKET, 7, 3),
    EGG(Material.EGG, 10, 4),
    PAINT(Material.PLAYER_HEAD, 100, 0),
    HAMMER(Material.PLAYER_HEAD, 100, 0),
    NAIL(Material.PLAYER_HEAD, 100, 0),
    COW_FEED(Material.PLAYER_HEAD, 1, 0),
    CHICKEN_FEED(Material.PLAYER_HEAD, 4, 0);
    @Getter(value = lombok.AccessLevel.NONE)
    private final ItemStack itemStack;
    private final int sellPrice;
    private final int xpGiven;

    ItemType(Material material, int sellPrice, int xpGiven) {
        this.itemStack = Utils.getItemStack(Utils.thing2(name()), material);
        this.sellPrice = sellPrice;
        this.xpGiven = xpGiven;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}