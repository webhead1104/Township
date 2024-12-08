package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;


@Getter
@AllArgsConstructor
public enum AnimalType {

    COWSHED(Msg.format("<gold>Cowshed"), MenuItems.cow, MenuItems.milk, MenuItems.cowFeed, ItemType.COW_FEED, ItemType.MILK, WorldTileType.COWSHED, 1, 3, "cowshed"),
    CHICKEN_COOP(Msg.format("<gold>Chicken Coop"), MenuItems.chicken, MenuItems.egg, MenuItems.chickenFeed, ItemType.CHICKEN_FEED, ItemType.EGG, WorldTileType.CHICKEN_COOP, 5, 4, "chicken_coop");
    private final Component menuTitle;
    private final ItemStack animalItemStack;
    private final ItemStack productItemStack;
    private final ItemStack feedItemStack;
    private final ItemType feedType;
    private final ItemType productType;
    private final WorldTileType tileType;
    private final int levelNeeded;
    private final int xpGiven;
    private final String ID;
}
