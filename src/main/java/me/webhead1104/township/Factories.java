package me.webhead1104.township;

import me.flame.menus.menu.PaginatedMenu;
import me.flame.menus.modifiers.Modifier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.EnumSet;

public class Factories {
    Township plugin;
    Items items;

    public Factories(Township plugin) {
        this.plugin = plugin;
        this.items = plugin.getItems();
    }

    public PaginatedMenu feedmill = PaginatedMenu.create(ChatColor.GOLD +"Feed Mill",5,1,EnumSet.allOf(Modifier.class));
    public PaginatedMenu bakery = PaginatedMenu.create(ChatColor.GOLD +"Bakery",5,1, EnumSet.allOf(Modifier.class));
    public PaginatedMenu dairyFactory = PaginatedMenu.create(ChatColor.GOLD +"Dairy Factory",5,1, EnumSet.allOf(Modifier.class));
    public PaginatedMenu sugarFactory = PaginatedMenu.create(ChatColor.GOLD +"Sugar Factory",5,1, EnumSet.allOf(Modifier.class));

    public void feedmill(Player player) {
        feedmill.setItem(11,items.completed);
        feedmill.setItem(12,items.completed);
        feedmill.setItem(13,items.completed);
        feedmill.setItem(14,items.completed);
        feedmill.setItem(15,items.completed);
        feedmill.setItem(39,items.cowfeedrecipe);
        feedmill.setItem(40,items.chickenfeedrecipe);
        feedmill.setItem(41,items.sheepfeedrecipe);
        feedmill.setItem(45,items.backButton);
        feedmill.open(player);
    }

    public void bakery(Player player) {
        bakery.setItem(11,items.completed);
        bakery.setItem(12,items.completed);
        bakery.setItem(13,items.completed);
        bakery.setItem(14,items.completed);
        bakery.setItem(15,items.completed);
        bakery.setItem(39,items.breadRecipe);
        bakery.setItem(40,items.cookieRecipe);
        bakery.setItem(41,items.bagelRecipe);
        bakery.setItem(45,items.backButton);
        bakery.open(player);
    }

    public void dairyFactory(Player player) {
        dairyFactory.setItem(11,items.completed);
        dairyFactory.setItem(12,items.completed);
        dairyFactory.setItem(13,items.completed);
        dairyFactory.setItem(14,items.completed);
        dairyFactory.setItem(15,items.completed);
        dairyFactory.setItem(39,items.creamRecipe);
        dairyFactory.setItem(40,items.cheeseRecipe);
        dairyFactory.setItem(41,items.butterRecipe);
        dairyFactory.setItem(42,items.yogurtRecipe);
        dairyFactory.setItem(45,items.backButton);
        dairyFactory.open(player);
    }

    public void sugarFactory(Player player) {
        sugarFactory.setItem(11,items.completed);
        sugarFactory.setItem(12,items.completed);
        sugarFactory.setItem(13,items.completed);
        sugarFactory.setItem(14,items.completed);
        sugarFactory.setItem(15,items.completed);
        sugarFactory.setItem(39,items.sugarRecipe);
        sugarFactory.setItem(40,items.syrupRecipe);
        sugarFactory.setItem(41,items.caramelRecipe);
        sugarFactory.setItem(45,items.backButton);
        sugarFactory.open(player);
    }


}
