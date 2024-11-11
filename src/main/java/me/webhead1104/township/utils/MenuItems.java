package me.webhead1104.township.utils;

import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@NoArgsConstructor
public class MenuItems {

    public static final ItemStack cow = new ItemBuilder(Material.COW_SPAWN_EGG, MM."<gold>Cow", "cow").build();
    public static final ItemStack cowFeed = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Cow Feed", List.of(MM."<gold>0"), "cow_feed").build();
    public static final ItemStack milk = new ItemBuilder(Material.MILK_BUCKET, MM."<gold>Milk", List.of(MM."<gold>1", MM."<green>Click to pick up!"), "milk").build();
    public static final ItemStack chicken = new ItemBuilder(Material.CHICKEN_SPAWN_EGG, MM."<RED>Chicken", "chicken").build();
    public static final ItemStack chickenFeed = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Chicken Feed", List.of(MM."<gold>0"), "chicken_feed").build();
    public static final ItemStack egg = new ItemBuilder(Material.EGG, MM."<gold>Egg", List.of(MM."<gold>1", MM."<green>Click to pick up!"), "egg").build();

    public static final ItemStack arrowUp = new ItemBuilder(Material.ARROW, MM."<dark_green>Click to scroll up!", "arrow_up").build();
    public static final ItemStack arrowDown = new ItemBuilder(Material.ARROW, MM."<dark_green>Click to scroll down!", "arrow_down").build();
    public static final ItemStack arrowLeft = new ItemBuilder(Material.ARROW, MM."<dark_green>Click to scroll left!", "arrow_left").build();
    public static final ItemStack arrowRight = new ItemBuilder(Material.ARROW, MM."<dark_green>Click to scroll right!", "arrow_right").build();

    public static final ItemStack levelAndPop = new ItemBuilder(Material.BLUE_CONCRETE, MM."<aqua>Level 0", List.of(MM."<red>Population 0"), "level_population").build();
    public static final ItemStack coinsAndCash = new ItemBuilder(Material.GOLD_BLOCK, MM."<yellow>Coins 0", List.of(MM."<green>Cash 0"), "coins_cash").build();
    public static final ItemStack profile = new ItemBuilder(Material.LIGHT_BLUE_CONCRETE, MM."null", "profile").build();
    public static final ItemStack glass = new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, MM."", "glass").build();
    public static final ItemStack township = new ItemBuilder(Material.GREEN_CONCRETE, MM."<green>Click to play township!", "township").build();
    public static final ItemStack backButton = new ItemBuilder(Material.BARRIER, MM."<RED>Click to go back!", "back_button").build();
    public static final ItemStack workingOn = new ItemBuilder(Material.COAL_BLOCK, MM."<green>Loading....", "working_on").build();
    public static final ItemStack completed = new ItemBuilder(Material.COAL_BLOCK, MM."<green>Loading....", "completed").build();
    public static final ItemStack expansionPrice = new ItemBuilder(Material.COAL_BLOCK, MM."<green>Loading....", "expansion_price").build();
    public static final ItemStack expansionPopulation = new ItemBuilder(Material.COAL_BLOCK, MM."<green>Loading....", "expansion_population").build();
    public static final ItemStack expansionBuy = new ItemBuilder(Material.COAL_BLOCK, MM."<green>Loading....", "expansion_buy").build();

    public static final ItemStack none = new ItemBuilder(Material.FARMLAND, MM."none plot", "none_plot").build();
    public static final ItemStack wheat = new ItemBuilder(Material.WHEAT, MM."<gold>Wheat", "wheat").build();
    public static final ItemStack corn = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Corn", "corn").build();
    public static final ItemStack carrot = new ItemBuilder(Material.CARROT, MM."<gold>Carrot", "carrot").build();
    public static final ItemStack sugarcane = new ItemBuilder(Material.SUGAR_CANE, MM."<gold>Sugarcane", "sugarcane").build();
    public static final ItemStack bread = new ItemBuilder(Material.BREAD, MM."<gold>Bread", "bread").build();
    public static final ItemStack cookie = new ItemBuilder(Material.COOKIE, MM."<gold>Cookie", "cookie").build();
    public static final ItemStack bagel = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Bagel", "bagel").build();
    public static final ItemStack cream = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Cream", "cream").build();
    public static final ItemStack cheese = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Cheese", "cheese").build();
    public static final ItemStack butter = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Butter", "butter").build();
    public static final ItemStack yogurt = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Yogurt", "yogurt").build();
    public static final ItemStack sugar = new ItemBuilder(Material.SUGAR, MM."<gold>Sugar", "sugar").build();
    public static final ItemStack syrup = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Syrup", "syrup")
            .setSkullTextureURL("http://textures.minecraft.net/texture/90800ad4d854c4f5dba77c9a4f2ad6dc7ad1a0d195ac3ce12d00ffdde71d668e").build();
    public static final ItemStack caramel = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Caramel", "caramel").build();
    public static final ItemStack paint = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Paint bucket", "paint")
            .setSkullTextureURL("http://textures.minecraft.net/texture/fec5963e1f78f2f05943f4dd32224661374c220ecfde1e54754f5ee1e555dd").build();
    public static final ItemStack hammer = new ItemBuilder(Material.PLAYER_HEAD, MM."<gold>Hammer", "hammer")
            .setSkullTextureURL("http://textures.minecraft.net/texture/efdc6d3bad396e7abc57ef56e84e016d016b862747b9f59b055ac20e4e3a0930").build();
    public static final ItemStack nail = new ItemBuilder(Material.IRON_BARS, MM."<gold>Nail", "nail").build();
    //barn
    public static final ItemStack barnArrowUp = new ItemBuilder(Material.ARROW, MM."<dark_green>Click to scroll up!", "barn_arrow_up").build();
    public static final ItemStack barnArrowDown = new ItemBuilder(Material.ARROW, MM."<dark_green>Click to scroll down!", "barn_arrow_down").build();
    public static final ItemStack barnSell = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "barn_sell").build();
    public static final ItemStack barnIncreaseAmount = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "barn_increase").build();
    public static final ItemStack barnDecreaseAmount = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "barn_decrease").build();
    public static final ItemStack barnUpgrade = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "barn_upgrade").build();
    public static final ItemStack barnStorage = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "barn_storage").build();
    //trains
    public static final ItemStack trainEngine = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "train_engine").build();
    public static final ItemStack trainCar = new ItemBuilder(Material.COAL_BLOCK, MM."<red>Loading...", "train_car").build();

    public static class World {
        public static final ItemStack grass = new ItemBuilder(Material.GRASS_BLOCK, MM."", "grass").build();
        public static final ItemStack stone = new ItemBuilder(Material.STONE, MM."", "stone").build();
        public static final ItemStack sand = new ItemBuilder(Material.SAND, MM."", "sand").build();
        public static final ItemStack water = new ItemBuilder(Material.WATER_BUCKET, MM."", "water").build();
        public static final ItemStack road = new ItemBuilder(Material.BLACK_CONCRETE, MM."", "road").build();
        public static final ItemStack wood_plank = new ItemBuilder(Material.OAK_PLANKS, MM."", "wood_plank").build();
        public static final ItemStack expansion = new ItemBuilder(Material.PODZOL, MM."Expansion", "expansion").build();
        public static final ItemStack plot = new ItemBuilder(Material.FARMLAND, MM."plot", "plot_none").build();
        public static final ItemStack barn = new ItemBuilder(Material.PLAYER_HEAD, MM."<blue>Barn", "barn")
                .setSkullTextureURL("http://textures.minecraft.net/texture/4a9953ce945da6e2fb80030e754fa8e092c4e9ecd0548f938c3497a808e41a48").build();
        public static final ItemStack train = new ItemBuilder(Material.PLAYER_HEAD, MM."Trains", "train").build();
        //animals
        public static final ItemStack cowshed = new ItemBuilder(Material.PLAYER_HEAD, MM."<white>Cowshed", "cowshed").build();
        public static final ItemStack chicken_coop = new ItemBuilder(Material.PLAYER_HEAD, MM."<white>Chicken Coop", "chicken_coop").build();
        //factories
        public static final ItemStack bakery = new ItemBuilder(Material.BREAD, MM."<white>Bakery", "bakery").build();
        public static final ItemStack feed_mill = new ItemBuilder(Material.WHEAT, MM."<white>Feed Mill", "feed_mill").build();
        public static final ItemStack dairy = new ItemBuilder(Material.MILK_BUCKET, MM."<white>Dairy Factory", "dairy_factory").build();
        public static final ItemStack sugar = new ItemBuilder(Material.SUGAR, MM."<white>Sugar Factory", "sugar_factory").build();
    }

    public static class Recipes {
        //bakery
        public static final ItemStack bread = new ItemBuilder(Material.BREAD, MM."<gold>Bread", List.of(MM."<white>2 wheat"), "bread_recipe").build();
        public static final ItemStack cookie = new ItemBuilder(Material.BREAD, MM."<gold>Cookie", List.of(MM."<white>2 wheat", MM."<white>2 eggs"), "cookie_recipe").build();
        public static final ItemStack bagel = new ItemBuilder(Material.BREAD, MM."<gold>Bagel", List.of(MM."<white>2 wheat", MM."<white>1 sugar", MM."<white>3 eggs"), "bagel_recipe").build();
        //feed mill
        public static final ItemStack cowFeed = new ItemBuilder(Material.WHEAT, MM."<gold>Cow Feed", List.of(MM."<white>2 wheat", MM."<white>1 corn"), "cow_feed_recipe").build();
        public static final ItemStack chickenFeed = new ItemBuilder(Material.EGG, MM."<gold>Chicken Feed", List.of(MM."<white>2 wheat", MM."<white>1 carrots"), "chicken_feed_recipe").build();
        //dairy
        public static final ItemStack cream = new ItemBuilder(Material.MAGMA_CREAM, MM."<gold>Cream", List.of(MM."<gold>1 milk"), "cream_recipe").build();
        public static final ItemStack cheese = new ItemBuilder(Material.GOLD_NUGGET, MM."<gold>Cheese", List.of(MM."<gold>2 milk"), "cheese_recipe").build();
        public static final ItemStack butter = new ItemBuilder(Material.GOLD_INGOT, MM."<gold>Butter", List.of(MM."<gold>3 milk"), "butter_recipe").build();
        public static final ItemStack yogurt = new ItemBuilder(Material.MILK_BUCKET, MM."<gold>Yogurt", List.of(MM."<gold>4 milk"), "yogurt_recipe").build();
        //sugar
        public static final ItemStack sugar = new ItemBuilder(Material.SUGAR, MM."<gold>Sugar", List.of(MM."<gold>1 sugarcane"), "sugar_recipe").build();
        public static final ItemStack syrup = new ItemBuilder(Material.BROWN_CANDLE, MM."<gold>Syrup", List.of(MM."<gold>2 sugarcane"), "syrup_recipe").build();
        public static final ItemStack caramel = new ItemBuilder(Material.BROWN_CONCRETE, MM."<gold>Caramel", List.of(MM."<gold>3 sugarcane"), "caramel_recipe").build();
    }
}