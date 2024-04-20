package me.webhead1104.township.data.enums;

import me.flame.menus.items.MenuItem;
import me.webhead1104.township.utils.Items;
import org.bukkit.ChatColor;

public enum FactoriesEnum {

    NONE("none",Items.air,"NONE"),
    BAKERY(ChatColor.GOLD + "Bakery", Items.bakeryMenuItem,"BAKERY"),
    FEED_MILL(ChatColor.GOLD + "Feed Mill",Items.feedmillMenuItem,"FEED_MILL"),
    DAIRY_FACTORY(ChatColor.GOLD + "Dairy Factory",Items.dairyMenuItem,"DAIRY_FACTORY"),
    SUGAR_FACTORY(ChatColor.GOLD + "Sugar Factory",Items.sugarMenuItem,"SUGAR_FACTORY");

    private final String factoryName;
    private final MenuItem menuItem;
    private final String ID;
    FactoriesEnum(String factoryName, MenuItem menuItem, String ID) {
        this.factoryName = factoryName;
        this.menuItem = menuItem;
        this.ID = ID;
    }

    public String getFactoryName() {return factoryName;}
    public String getID() {return ID;}
    public MenuItem getMenuItem() {return menuItem;}
}
