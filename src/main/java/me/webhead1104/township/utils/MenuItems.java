package me.webhead1104.township.utils;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.CustomData;

import java.util.List;
import static me.webhead1104.township.data.enums.ItemType.*;
import static net.cytonic.cytosis.utils.MiniMessageTemplate.MM;

public class MenuItems {

    public MenuItems() {}

    public static ItemStack air = ItemStack.of(Material.AIR);
    public static ItemStack cow = ItemStack.of(Material.COW_SPAWN_EGG).builder().customName(MM."<gold>Cow").build();
    public static ItemStack cowshedFeed = ItemStack.of(COW_FEED.getItemMaterial()).builder().customName(COW_FEED.getItemName()).lore(MM."<gold>0").build();
    public static ItemStack milk = ItemStack.of(MILK.getItemMaterial()).builder().customName(MILK.getItemName()).lore(List.of(MM."<gold>1",MM."<green>Click to pick up!")).build();
    public static ItemStack chicken = ItemStack.of(Material.CHICKEN_SPAWN_EGG).builder().customName(MM."<RED>Chicken").build();
    public static ItemStack chickencoopFeed = ItemStack.of(CHICKEN_FEED.getItemMaterial()).builder().customName(CHICKEN_FEED.getItemName()).lore(MM."<gold>0").build();
    public static ItemStack egg = ItemStack.of(EGG.getItemMaterial()).builder().customName(EGG.getItemName()).lore(List.of(MM."<gold>1", MM."<green>Click to pick up!")).build();

    public static ItemStack arrowUP = ItemStack.of(Material.ARROW).builder().customName(MM."<dark_green>Click to scroll up!")
            .set(ItemComponent.CUSTOM_DATA, new CustomData(CompoundBinaryTag.builder().putString("arrow","up").build())).build();
    public static ItemStack arrowDOWN = ItemStack.of(Material.ARROW).builder().customName(MM."<dark_green>Click to scroll down!")
            .set(ItemComponent.CUSTOM_DATA, new CustomData(CompoundBinaryTag.builder().putString("arrow","down").build())).build();
    public static ItemStack arrowLEFT = ItemStack.of(Material.ARROW).builder().customName(MM."<dark_green>Click to scroll left!")
            .set(ItemComponent.CUSTOM_DATA, new CustomData(CompoundBinaryTag.builder().putString("arrow","left").build())).build();
    public static ItemStack arrowRIGHT = ItemStack.of(Material.ARROW).builder().customName(MM."<dark_green>Click to scroll right!")
            .set(ItemComponent.CUSTOM_DATA, new CustomData(CompoundBinaryTag.builder().putString("arrow","right").build())).build();

    public static ItemStack levelAndPop = ItemStack.of(Material.BLUE_CONCRETE).builder().customName(MM."<aqua>Level 0").lore(MM."<red>Population 0").build();
    public static ItemStack levelMenuMiddleItem = ItemStack.of(Material.BLUE_CONCRETE).builder().customName(MM."<aqua>Level null").lore(MM."<gray>Levels coming soon!").build();
    public static ItemStack coinsAndCash = ItemStack.of(Material.GOLD_BLOCK).builder().customName(MM."<yellow>Coins 0").lore(MM."<green>Cash 0").build();
    public static ItemStack profile = ItemStack.of(Material.LIGHT_BLUE_CONCRETE).builder().customName(MM."null").build();
    public static ItemStack glass = ItemStack.of(Material.WHITE_STAINED_GLASS_PANE).builder().customName(MM."").build();
    public static ItemStack township = ItemStack.of(Material.GREEN_CONCRETE).builder().customName(MM."<green>Click to play township!").build();
    public static ItemStack backButton = ItemStack.of(Material.BARRIER).builder().customName(MM."<RED>Click to go back!").build();
    public static ItemStack grass = ItemStack.of(Material.GRASS_BLOCK).builder().customName(MM."").build();
    public static ItemStack workingOn = ItemStack.of(Material.BLUE_CONCRETE).builder().customName(MM."<green>Loading....").build();
    public static ItemStack completed = ItemStack.of(Material.BROWN_CONCRETE).builder().customName(MM."<green>Loading....").build();
    //cowshed
    public static ItemStack cowshedMenuItem = ItemStack.of(Material.PLAYER_HEAD).builder().customName(MM."<white>Cowshed").build();
    public static ItemStack chickenMenuItem = ItemStack.of(Material.PLAYER_HEAD).builder().customName(MM."<white>Chicken Coop").build();
    //bakery
    public static ItemStack bakeryMenuItem = ItemStack.of(Material.BREAD).builder().customName(MM."<white>Bakery").build();
    public static ItemStack breadRecipe = ItemStack.of(Material.BREAD).builder().customName(MM."<gold>Bread").lore(List.of(MM."<white>2 wheat")).build();
    public static ItemStack cookieRecipe = ItemStack.of(Material.BREAD).builder().customName(MM."<gold>Cookie").lore(List.of(MM."<white>2 wheat", MM."<white>2 eggs")).build();
    public static ItemStack bagelRecipe = ItemStack.of(Material.BREAD).builder().customName(MM."<gold>Bagel").lore(List.of(MM."<white>2 wheat", MM."<white>1 sugar", MM."<white>3 eggs")).build();
    //feed mill
    public static ItemStack feedmillMenuItem = ItemStack.of(Material.WHEAT).builder().customName(MM."<white>Feed Mill").build();
    public static ItemStack cowfeedrecipe = ItemStack.of(Material.WHEAT).builder().customName(MM."<gold>Cow Feed").lore(List.of(MM."<white>2 wheat", MM."<white>1 corn")).build();
    public static ItemStack chickenfeedrecipe = ItemStack.of(Material.EGG).builder().customName(MM."<gold>Chicken Feed").lore(List.of(MM."<white>2 wheat", MM."<white>1 carrots")).build();
    //dairy
    public static ItemStack dairyMenuItem = ItemStack.of(Material.MILK_BUCKET).builder().customName(MM."<white>Dairy Factory").build();
    public static ItemStack creamRecipe = ItemStack.of(Material.MAGMA_CREAM).builder().customName(MM."<gold>Cream").lore(List.of(MM."<gold>1 milk")).build();
    public static ItemStack cheeseRecipe = ItemStack.of(Material.GOLD_NUGGET).builder().customName(MM."<gold>Cheese").lore(List.of(MM."<gold>2 milk")).build();
    public static ItemStack butterRecipe = ItemStack.of(Material.GOLD_INGOT).builder().customName(MM."<gold>Butter").lore(List.of(MM."<gold>3 milk")).build();
    public static ItemStack yogurtRecipe = ItemStack.of(Material.MILK_BUCKET).builder().customName(MM."<gold>Yogurt").lore(List.of(MM."<gold>4 milk")).build();
    //sugar
    public static ItemStack sugarMenuItem = ItemStack.of(Material.SUGAR).builder().customName(MM."<white>Sugar Factory").build();
    public static ItemStack sugarRecipe = ItemStack.of(Material.SUGAR).builder().customName(MM."<gold>Sugar").lore(List.of(MM."<gold>1 sugarcane")).build();
    public static ItemStack syrupRecipe = ItemStack.of(Material.BROWN_CANDLE).builder().customName(MM."<gold>Syrup").lore(List.of(MM."<gold>2 sugarcane")).build();
    public static ItemStack caramelRecipe = ItemStack.of(Material.BROWN_CONCRETE).builder().customName(MM."<gold>Caramel").lore(List.of(MM."<gold>3 sugarcane")).build();
}