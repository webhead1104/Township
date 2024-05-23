package me.webhead1104.township.data.enums;

import me.flame.menus.items.MenuItem;
import me.webhead1104.township.utils.MenuItems;
import org.bukkit.ChatColor;

import java.util.List;

public enum FactoryType {

    NONE("none", MenuItems.air,"NONE",List.of(MenuItems.air)),
    BAKERY(ChatColor.GOLD + "Bakery", MenuItems.bakeryMenuItem,"BAKERY",List.of(MenuItems.breadRecipe,MenuItems.cookieRecipe,MenuItems.bagelRecipe)),
    FEED_MILL(ChatColor.GOLD + "Feed Mill",MenuItems.feedmillMenuItem,"FEED_MILL",List.of(MenuItems.cowfeedrecipe,MenuItems.chickenfeedrecipe)),
    DAIRY_FACTORY(ChatColor.GOLD + "Dairy Factory",MenuItems.dairyMenuItem,"DAIRY_FACTORY",List.of(MenuItems.creamRecipe,MenuItems.cheeseRecipe,MenuItems.butterRecipe,MenuItems.yogurtRecipe)),
    SUGAR_FACTORY(ChatColor.GOLD + "Sugar Factory",MenuItems.sugarMenuItem,"SUGAR_FACTORY",List.of(MenuItems.sugarRecipe,MenuItems.syrupRecipe,MenuItems.caramelRecipe));

    private final String factoryName;
    private final MenuItem menuItem;
    private final String ID;
    private final List<MenuItem> recipes;
    FactoryType(String factoryName, MenuItem menuItem, String ID, List<MenuItem> recipes) {
        this.factoryName = factoryName;
        this.menuItem = menuItem;
        this.ID = ID;
        this.recipes = recipes;
    }

    public String getFactoryName() {return factoryName;}
    public String getID() {return ID;}
    public MenuItem getMenuItem() {return menuItem;}
    public List<MenuItem> getRecipes() {return recipes;}
}
