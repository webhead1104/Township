package me.webhead1104.township.data.enums;

import lombok.Getter;
import me.webhead1104.township.utils.MenuItems;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;

import static net.cytonic.cytosis.utils.MiniMessageTemplate.MM;

@Getter
public enum AnimalType {

    NONE(MM."none", MenuItems.air, MenuItems.air, MenuItems.air, MenuItems.air, ItemType.AIR, "NONE"),
    COWSHED(MM."<gold>Cowshed", MenuItems.cow, MenuItems.cow, MenuItems.milk, MenuItems.cowshedFeed, ItemType.COW_FEED, "COWSHED"),
    CHICKEN_COOP(MM."<gold>Chicken Coop", MenuItems.chicken, MenuItems.chicken, MenuItems.egg, MenuItems.chickencoopFeed, ItemType.CHICKEN_FEED, "CHICKEN_COOP");

    private final Component animalName;
    private final ItemStack menuItem;
    private final ItemStack animal;
    private final ItemStack product;
    private final ItemStack feed;
    private final ItemType feedType;
    private final String ID;

    AnimalType(Component animalName, ItemStack menuItem, ItemStack animal, ItemStack product, ItemStack feed, ItemType feedType, String ID) {
        this.animalName = animalName;
        this.menuItem = menuItem;
        this.animal = animal;
        this.product = product;
        this.feed = feed;
        this.feedType = feedType;
        this.ID = ID;
    }
}