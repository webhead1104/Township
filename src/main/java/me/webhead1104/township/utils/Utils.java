package me.webhead1104.township.utils;

import me.webhead1104.township.data.enums.AnimalsEnum;
import me.webhead1104.township.data.enums.FactoriesEnum;
import me.webhead1104.township.data.enums.ItemsEnum;
import static me.webhead1104.township.data.enums.ItemsEnum.*;
import static me.webhead1104.township.data.enums.AnimalsEnum.*;
import static me.webhead1104.township.data.enums.FactoriesEnum.*;

public class Utils {

    public static ItemsEnum getItem(String itemID) {
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

    public static AnimalsEnum getAnimal(String animalID) {
        switch (animalID.toUpperCase()) {
            case "COWSHED" -> {return COWSHED;}
            case "CHICKEN_COOP" -> {return CHICKEN_COOP;}
            default -> {return AnimalsEnum.NONE;}
        }
    }

    public static FactoriesEnum getFactory(String factoryID) {
        switch (factoryID.toUpperCase()) {
            case "BAKERY" -> {return BAKERY;}
            case "FEED_MILL" -> {return FEED_MILL;}
            case "DAIRY_FACTORY" -> {return DAIRY_FACTORY;}
            case "SUGAR_FACTORY" -> {return SUGAR_FACTORY;}
            default -> {return FactoriesEnum.NONE;}
        }
    }

    public static String switchToID(String string) {
        switch (string.toUpperCase()) {
            case "MILK" -> {return "COWSHED";}
            case "EGG" -> {return "EGG";}
            default -> {return "none";}
        }
    }
    public static AnimalsEnum switchToAnimal(int var0){
        switch (var0) {
            case 1 -> {return COWSHED;}
            case 2 -> {return CHICKEN_COOP;}
            default -> {return AnimalsEnum.NONE;}
        }
    }

    public static FactoriesEnum switchToFactory(int var0){
        switch (var0) {
            case 1 -> {return BAKERY;}
            case 2 -> {return FEED_MILL;}
            case 3 -> {return DAIRY_FACTORY;}
            case 4 -> {return SUGAR_FACTORY;}
            default -> {return FactoriesEnum.NONE;}
        }
    }
}
