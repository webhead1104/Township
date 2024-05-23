package me.webhead1104.township.data.enums;

import me.flame.menus.items.MenuItem;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.ChatColor;

public enum AnimalType {

    NONE("none", MenuItems.air, MenuItems.air,MenuItems.air,MenuItems.air, ItemType.AIR,"NONE"),
    COWSHED(ChatColor.GOLD + "Cowshed", MenuItems.cowshedMenuItem,MenuItems.cow,MenuItems.milk,MenuItems.cowshedFeed,ItemType.COW_FEED,"COWSHED"),
    CHICKEN_COOP(ChatColor.GOLD + "Chicken Coop", MenuItems.chickenMenuItem,MenuItems.chicken,MenuItems.egg,MenuItems.chickencoopFeed,ItemType.CHICKEN_FEED,"CHICKEN_COOP");

    private final String animalName;
    private final MenuItem menuItem;
    private final MenuItem animal;
    private final MenuItem product;
    private final MenuItem feed;
    private final ItemType feedType;
    private final String ID;
    AnimalType(String animalName, MenuItem menuItem, MenuItem animal, MenuItem product, MenuItem feed, ItemType feedType, String ID) {
        this.animalName = animalName;
        this.menuItem = menuItem;
        this.animal = animal;
        this.product = product;
        this.feed = feed;
        this.feedType = feedType;
        this.ID = ID;
    }

    public String getAnimalName() {return animalName;}
    public MenuItem getMenuItem() {return menuItem;}
    public MenuItem getAnimal() {return animal;}
    public MenuItem getProduct() {return product;}
    public MenuItem getFeed() {return feed;}
    public ItemType getFeedType() {return feedType;}
    public String getID() {return ID;}
}