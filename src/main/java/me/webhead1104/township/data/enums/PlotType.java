package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public enum PlotType {
    NONE(ItemType.NONE, MenuItems.World.plot, -1, -1, -1, "none_plot"),
    WHEAT(ItemType.WHEAT, MenuItems.wheat, 0, 1, 1, "wheat_plot"),
    CORN(ItemType.CORN, MenuItems.corn, 1, 3, 1, "corn_plot"),
    CARROT(ItemType.CARROT, MenuItems.carrot, 2, 4, 2, "carrot_plot"),
    SUGARCANE(ItemType.SUGARCANE, MenuItems.sugarcane, 3, 7, 3, "sugarcane_plot");
    private final ItemType itemType;
    private final ItemStack menuItem;
    private final int price;
    private final int levelNeeded;
    private final int xpGiven;
    private final String id;
}
