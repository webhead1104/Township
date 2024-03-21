package me.webhead1104.township;

import me.flame.menus.menu.PaginatedMenu;
import me.flame.menus.modifiers.Modifier;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.EnumSet;

public class Factories {

    Township plugin;
    public Factories(Township plugin) {
        this.plugin = plugin;
    }
    public PaginatedMenu feedmill = PaginatedMenu.create(ChatColor.GOLD +"Feed Mill",5,1,EnumSet.allOf(Modifier.class));
    public PaginatedMenu bakery = PaginatedMenu.create(ChatColor.GOLD +"Bakery",5,1, EnumSet.allOf(Modifier.class));
    public PaginatedMenu dairyFactory = PaginatedMenu.create(ChatColor.GOLD +"Dairy Factory",5,1, EnumSet.allOf(Modifier.class));
    public PaginatedMenu sugarFactory = PaginatedMenu.create(ChatColor.GOLD +"Sugar Factory",5,1, EnumSet.allOf(Modifier.class));

    public void feedmill(Player player) {
        feedmill.setItem(11,plugin.getItems().completed);
        feedmill.setItem(12,plugin.getItems().completed);
        feedmill.setItem(13,plugin.getItems().completed);
        feedmill.setItem(14,plugin.getItems().completed);
        feedmill.setItem(15,plugin.getItems().completed);
        feedmill.setItem(39,plugin.getItems().cowfeedrecipe);
        feedmill.setItem(40,plugin.getItems().chickenfeedrecipe);
        feedmill.setItem(41,plugin.getItems().sheepfeedrecipe);
        feedmill.setItem(45,plugin.getItems().backButton);
        feedmill.open(player);
    }

    public void bakery(Player player) {
        bakery.setItem(11,plugin.getItems().completed);
        bakery.setItem(12,plugin.getItems().completed);
        bakery.setItem(13,plugin.getItems().completed);
        bakery.setItem(14,plugin.getItems().completed);
        bakery.setItem(15,plugin.getItems().completed);
        bakery.setItem(39,plugin.getItems().breadRecipe);
        bakery.setItem(40,plugin.getItems().cookieRecipe);
        bakery.setItem(41,plugin.getItems().bagelRecipe);
        bakery.setItem(45,plugin.getItems().backButton);
        bakery.open(player);
    }

    public void dairyFactory(Player player) {
        dairyFactory.setItem(11,plugin.getItems().completed);
        dairyFactory.setItem(12,plugin.getItems().completed);
        dairyFactory.setItem(13,plugin.getItems().completed);
        dairyFactory.setItem(14,plugin.getItems().completed);
        dairyFactory.setItem(15,plugin.getItems().completed);
        dairyFactory.setItem(39,plugin.getItems().creamRecipe);
        dairyFactory.setItem(40,plugin.getItems().cheeseRecipe);
        dairyFactory.setItem(41,plugin.getItems().butterRecipe);
        dairyFactory.setItem(42,plugin.getItems().yogurtRecipe);
        dairyFactory.setItem(45,plugin.getItems().backButton);
        dairyFactory.open(player);
    }

    public void sugarFactory(Player player) {
        sugarFactory.setItem(11,plugin.getItems().completed);
        sugarFactory.setItem(12,plugin.getItems().completed);
        sugarFactory.setItem(13,plugin.getItems().completed);
        sugarFactory.setItem(14,plugin.getItems().completed);
        sugarFactory.setItem(15,plugin.getItems().completed);
        sugarFactory.setItem(39,plugin.getItems().sugarRecipe);
        sugarFactory.setItem(40,plugin.getItems().syrupRecipe);
        sugarFactory.setItem(41,plugin.getItems().caramelRecipe);
        sugarFactory.setItem(45,plugin.getItems().backButton);
        sugarFactory.open(player);
    }
}