package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum ItemType {
    NONE(Material.BARRIER, -1),
    WHEAT(Material.WHEAT, 1),
    CORN(Material.PLAYER_HEAD, 3),
    CARROT(Material.CARROT, 5),
    SUGARCANE(Material.SUGAR_CANE, 7),
    BREAD(Material.BREAD, 5),
    COOKIE(Material.COOKIE, 44),
    BAGEL(Material.PLAYER_HEAD, 55),
    CREAM(Material.PLAYER_HEAD, 12),
    CHEESE(Material.PLAYER_HEAD, 25),
    SUGAR(Material.SUGAR, 14),
    MILK(Material.MILK_BUCKET, 7),
    EGG(Material.EGG, 10),
    PAINT(Material.PLAYER_HEAD, 100),
    HAMMER(Material.PLAYER_HEAD, 100),
    NAIL(Material.PLAYER_HEAD, 100),
    COW_FEED(Material.PLAYER_HEAD, 1),
    CHICKEN_FEED(Material.PLAYER_HEAD, 4);
    @Getter(value = lombok.AccessLevel.NONE)
    private final ItemStack itemStack;
    private final int sellPrice;

    ItemType(Material material, int sellPrice) {
        this.itemStack = Utils.getItemStack(Utils.thing2(name()), material);
        this.sellPrice = sellPrice;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }
}