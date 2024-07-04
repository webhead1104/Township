package me.webhead1104.township.data.enums;

import lombok.Getter;
import net.cytonic.cytosis.utils.MiniMessageTemplate;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;

@Getter
public enum ItemType {
    AIR(MiniMessageTemplate.MM."<RED>if you see this what did you do", Material.AIR, "AIR"),
    WHEAT(MiniMessageTemplate.MM."<gold>Wheat", Material.WHEAT, "WHEAT"),
    CORN(MiniMessageTemplate.MM."<gold>Corn", Material.PLAYER_HEAD, "CORN"),
    CARROT(MiniMessageTemplate.MM."<gold>Carrot", Material.CARROT, "CARROT"),
    SUGARCANE(MiniMessageTemplate.MM."<gold>Sugarcane", Material.SUGAR_CANE, "SUGARCANE"),
    BREAD(MiniMessageTemplate.MM."<gold>Bread", Material.BREAD, "BREAD"),
    COOKIE(MiniMessageTemplate.MM."<gold>Cookie", Material.COOKIE, "COOKIE"),
    BAGEL(MiniMessageTemplate.MM."<gold>Bagel", Material.PLAYER_HEAD, "BAGEL"),
    COW_FEED(MiniMessageTemplate.MM."<gold>Cow Feed", Material.PLAYER_HEAD, "COW_FEED"),
    CHICKEN_FEED(MiniMessageTemplate.MM."<gold>Chicken Feed", Material.PLAYER_HEAD, "CHICKEN_FEED"),
    CREAM(MiniMessageTemplate.MM."<gold>Cream", Material.PLAYER_HEAD, "CREAM"),
    CHEESE(MiniMessageTemplate.MM."<gold>Cheese", Material.PLAYER_HEAD, "CHEESE"),
    BUTTER(MiniMessageTemplate.MM."<gold>Butter", Material.PLAYER_HEAD, "BUTTER"),
    YOGURT(MiniMessageTemplate.MM."<gold>Yogurt", Material.PLAYER_HEAD, "YOGURT"),
    SUGAR(MiniMessageTemplate.MM."<gold>Sugar", Material.SUGAR, "SUGAR"),
    SYRUP(MiniMessageTemplate.MM."<gold>Syrup", Material.PLAYER_HEAD, "SYRUP"),
    CARAMEL(MiniMessageTemplate.MM."<gold>Caramel", Material.PLAYER_HEAD, "CARAMEL"),
    MILK(MiniMessageTemplate.MM."<gold>Milk", Material.MILK_BUCKET, "MILK"),
    EGG(MiniMessageTemplate.MM."<gold>Egg", Material.EGG, "EGG");

    private final Component itemName;
    private final Material itemMaterial;
    private final String ID;

    ItemType(Component itemName, Material itemMaterial, String ID) {
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.ID = ID;
    }
}