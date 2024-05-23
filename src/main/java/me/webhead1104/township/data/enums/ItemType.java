package me.webhead1104.township.data.enums;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum ItemType {
    AIR(ChatColor.RED + "if you see this what did you do", Material.AIR, "AIR"),
    WHEAT(ChatColor.GOLD + "Wheat", Material.WHEAT, "WHEAT"),
    CORN(ChatColor.GOLD + "Corn", Material.PLAYER_HEAD, "CORN"),
    CARROT(ChatColor.GOLD + "Carrot", Material.CARROT, "CARROT"),
    SUGARCANE(ChatColor.GOLD + "Sugarcane", Material.SUGAR_CANE, "SUGARCANE"),
    BREAD(ChatColor.GOLD + "Bread", Material.BREAD, "BREAD"),
    COOKIE(ChatColor.GOLD + "Cookie", Material.COOKIE, "COOKIE"),
    BAGEL(ChatColor.GOLD + "Bagel", Material.PLAYER_HEAD, "BAGEL"),
    COW_FEED(ChatColor.GOLD + "Cow Feed", Material.PLAYER_HEAD, "COW_FEED"),
    CHICKEN_FEED(ChatColor.GOLD + "Chicken Feed", Material.PLAYER_HEAD, "CHICKEN_FEED"),
    CREAM(ChatColor.GOLD + "Cream", Material.PLAYER_HEAD, "CREAM"),
    CHEESE(ChatColor.GOLD + "Cheese", Material.PLAYER_HEAD, "CHEESE"),
    BUTTER(ChatColor.GOLD + "Butter", Material.PLAYER_HEAD, "BUTTER"),
    YOGURT(ChatColor.GOLD + "Yogurt", Material.PLAYER_HEAD, "YOGURT"),
    SUGAR(ChatColor.GOLD + "Sugar", Material.SUGAR, "SUGAR"),
    SYRUP(ChatColor.GOLD + "Syrup", Material.PLAYER_HEAD, "SYRUP"),
    CARAMEL(ChatColor.GOLD + "Caramel", Material.PLAYER_HEAD, "CARAMEL"),
    MILK(ChatColor.GOLD + "Milk", Material.MILK_BUCKET, "MILK"),
    EGG(ChatColor.GOLD + "Egg", Material.EGG, "EGG");

    private final String itemName;
    private final Material itemMaterial;
    private final String ID;

    ItemType(String itemName, Material itemMaterial, String ID) {
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.ID = ID;
    }

    public String getItemName() {return itemName;}

    public Material getItemMaterial() {return itemMaterial;}

    public String getID() {return ID;}
}