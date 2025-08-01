package me.webhead1104.township.data.enums;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.Getter;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;

@Getter
public enum PlotType {
    NONE(ItemType.NONE, getPlotItemStack(), -1, -1, -1, Duration.ZERO),
    WHEAT(ItemType.WHEAT, 0, 1, 1, Duration.ofMinutes(2)),
    CORN(ItemType.CORN, 1, 3, 1, Duration.ofMinutes(5)),
    CARROT(ItemType.CARROT, 2, 4, 2, Duration.ofMinutes(10)),
    SUGARCANE(ItemType.SUGARCANE, 3, 7, 3, Duration.ofMinutes(20));
    private final ItemType itemType;
    @Getter(value = lombok.AccessLevel.NONE)
    private final ItemStack menuItem;
    private final int price;
    private final int levelNeeded;
    private final int xpGiven;
    private final Duration time;

    PlotType(ItemType itemType, ItemStack menuItem, int price, int levelNeeded, int xpGiven, Duration time) {
        this.itemType = itemType;
        this.menuItem = menuItem;
        this.price = price;
        this.levelNeeded = levelNeeded;
        this.xpGiven = xpGiven;
        this.time = time;
    }

    PlotType(ItemType itemType, int price, int levelNeeded, int xpGiven, Duration time) {
        this.itemType = itemType;
        this.menuItem = itemType.getItemStack();
        this.price = price;
        this.levelNeeded = levelNeeded;
        this.xpGiven = xpGiven;
        this.time = time;
    }

    private static ItemStack getPlotItemStack() {
        ItemStack itemStack = ItemStack.of(Material.FARMLAND);
        itemStack.setData(DataComponentTypes.ITEM_NAME, Msg.format("Empty Plot"));
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(Msg.format("<green>You should plant something!"))));
        return itemStack;
    }

    public ItemStack getMenuItem() {
        return menuItem.clone();
    }
}
