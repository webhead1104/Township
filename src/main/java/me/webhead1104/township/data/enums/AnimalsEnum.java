package me.webhead1104.township.data.enums;

import me.flame.menus.items.MenuItem;
import me.webhead1104.township.utils.Items;
import org.bukkit.ChatColor;

public enum AnimalsEnum {

    NONE("none", Items.air, Items.air,Items.air,Items.air,ItemsEnum.AIR,"NONE"),
    COWSHED(ChatColor.GOLD + "Cowshed", Items.cowshedMenuItem,Items.cow,Items.milk,Items.cowshedFeed,ItemsEnum.COW_FEED,"COWSHED"),
    CHICKEN_COOP(ChatColor.GOLD + "Chicken Coop", Items.chickenMenuItem,Items.chicken,Items.egg,Items.chickencoopFeed,ItemsEnum.CHICKEN_FEED,"CHICKEN_COOP");

    private final String animalName;
    private final MenuItem menuItem;
    private final MenuItem animal;
    private final MenuItem product;
    private final MenuItem feed;
    private final ItemsEnum feedType;
    private final String ID;
    AnimalsEnum(String animalName, MenuItem menuItem, MenuItem animal, MenuItem product, MenuItem feed, ItemsEnum feedType, String ID) {
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
    public ItemsEnum getFeedType() {return feedType;}
    public String getID() {return ID;}
}
