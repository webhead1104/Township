package me.webhead1104.township.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@Getter
@AllArgsConstructor
public enum AnimalType {

    COWSHED(MM."<gold>Cowshed", MenuItems.cow, MenuItems.milk, MenuItems.cowFeed, ItemType.COW_FEED, ItemType.MILK, WorldTileType.COWSHED, "cowshed"),
    CHICKEN_COOP(MM."<gold>Chicken Coop", MenuItems.chicken, MenuItems.egg, MenuItems.chickenFeed, ItemType.CHICKEN_FEED, ItemType.EGG, WorldTileType.CHICKEN_COOP, "chicken_coop");

    private final Component menuTitle;
    private final ItemStack animalItemStack;
    private final ItemStack productItemStack;
    private final ItemStack feedItemStack;
    private final ItemType feedType;
    private final ItemType productType;
    private final WorldTileType tileType;
    private final String ID;
}
