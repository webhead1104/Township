package me.webhead1104.township.features.world.plots;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.dataLoaders.ItemType;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;
import java.util.List;

@Getter
public enum PlotType {
    NONE(Township.noneKey, getPlotItemStack(), -1, -1, -1, Duration.ZERO),
    WHEAT(Township.key("wheat"), 0, 1, 1, Duration.ofMinutes(2)),
    CORN(Township.key("corn"), 1, 3, 1, Duration.ofMinutes(5)),
    CARROT(Township.key("carrot"), 2, 4, 2, Duration.ofMinutes(10)),
    SUGARCANE(Township.key("sugarcane"), 3, 7, 3, Duration.ofMinutes(20)),
    COTTON(Township.key("cotton"), 4, 9, 4, Duration.ofMinutes(30));
    private final ItemType.Item item;
    private final ItemStack menuItem;
    private final int price;
    private final int levelNeeded;
    private final int xpGiven;
    private final Duration time;

    PlotType(Key key, ItemStack menuItem, int price, int levelNeeded, int xpGiven, Duration time) {
        this.item = ItemType.get(key);
        this.menuItem = menuItem;
        this.price = price;
        this.levelNeeded = levelNeeded;
        this.xpGiven = xpGiven;
        this.time = time;
    }

    PlotType(Key key, int price, int levelNeeded, int xpGiven, Duration time) {
        this.item = ItemType.get(key);
        this.menuItem = item.getItemStack();
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
