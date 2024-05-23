package me.webhead1104.township.utils;

import me.flame.menus.menu.Menu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.EnumSet;
import static me.webhead1104.township.data.enums.AnimalType.*;
import static me.webhead1104.township.data.enums.FactoryType.*;
import static me.webhead1104.township.data.enums.ItemType.*;

public class Utils {

    public static ItemType getItem(String itemID) {
        switch (itemID.toUpperCase()) {
            case "WHEAT" -> {return WHEAT;}
            case "CORN" -> {return CORN;}
            case "CARROT" -> {return CARROT;}
            case "SUGARCANE" -> {return SUGARCANE;}
            case "BREAD" -> {return BREAD;}
            case "COOKIE" -> {return COOKIE;}
            case "BAGEL" -> {return BAGEL;}
            case "COW_FEED" -> {return COW_FEED;}
            case "CHICKEN_FEED" -> {return CHICKEN_FEED;}
            case "CREAM" -> {return CREAM;}
            case "CHEESE" -> {return CHEESE;}
            case "BUTTER" -> {return BUTTER;}
            case "YOGURT" -> {return YOGURT;}
            case "SUGAR" -> {return SUGAR;}
            case "SYRUP" -> {return SYRUP;}
            case "CARAMEL" -> {return CARAMEL;}
            case "MILK" -> {return MILK;}
            case "EGG" -> {return EGG;}
            default -> {return AIR;}
        }
    }

    public static AnimalType getAnimal(String animalID) {
        switch (animalID.toUpperCase()) {
            case "COWSHED" -> {return COWSHED;}
            case "CHICKEN_COOP" -> {return CHICKEN_COOP;}
            default -> {return AnimalType.NONE;}
        }
    }

    public static FactoryType getFactory(String factoryID) {
        switch (factoryID.toUpperCase()) {
            case "BAKERY" -> {return BAKERY;}
            case "FEED_MILL" -> {return FEED_MILL;}
            case "DAIRY_FACTORY" -> {return DAIRY_FACTORY;}
            case "SUGAR_FACTORY" -> {return SUGAR_FACTORY;}
            default -> {return FactoryType.NONE;}
        }
    }

    public static String switchToID(String string) {
        switch (string.toUpperCase()) {
            case "MILK" -> {return "COWSHED";}
            case "EGG" -> {return "EGG";}
            default -> {return "none";}
        }
    }
    public static AnimalType switchToAnimal(int var0){
        switch (var0) {
            case 1 -> {return COWSHED;}
            case 2 -> {return CHICKEN_COOP;}
            default -> {return AnimalType.NONE;}
        }
    }

    public static FactoryType switchToFactory(int var0){
        switch (var0) {
            case 1 -> {return BAKERY;}
            case 2 -> {return FEED_MILL;}
            case 3 -> {return DAIRY_FACTORY;}
            case 4 -> {return SUGAR_FACTORY;}
            default -> {return FactoryType.NONE;}
        }
    }

    public void mainMenu(Player player) {
        Menu mainMenu = Menu.create(STR."\{ChatColor.GOLD}Main Menu", 5, EnumSet.allOf(Modifier.class));
        player.getInventory().clear();
        mainMenu.getFiller().fill(MenuItems.glass);
        mainMenu.setItem(22, MenuItems.township);
        mainMenu.open(player);
    }
}
