package me.webhead1104.township.utils;

import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;


@NoArgsConstructor
public class MenuItems {
    public static final ItemStack arrow = ItemBuilder.loading().id("arrow").build();

    public static final ItemStack levelAndPop = new ItemBuilder(Material.BLUE_CONCRETE, Msg.format("<aqua>Level 0"), List.of(Msg.format("<red>Population 0")), "level_population").build();
    public static final ItemStack coinsAndCash = new ItemBuilder(Material.GOLD_BLOCK, Msg.format("<yellow>Coins 0"), List.of(Msg.format("<green>Cash 0")), "coins_cash").build();
    public static final ItemStack profile = ItemBuilder.loading().id("profile").build();
    public static final ItemStack township = new ItemBuilder(Material.GREEN_CONCRETE, Msg.format("<green>Click to play township!"), "township").build();
    public static final ItemStack backButton = new ItemBuilder(Material.BARRIER, Msg.format("<red>Click to go back!"), "back_button").build();
    public static final ItemStack workingOn = ItemBuilder.loading().id("working_on").build();
    public static final ItemStack waiting = ItemBuilder.loading().id("waiting").build();
    public static final ItemStack completed = ItemBuilder.loading().id("completed").build();
    public static final ItemStack expansionPrice = ItemBuilder.loading().id("expansion_price").build();
    public static final ItemStack expansionPopulation = ItemBuilder.loading().id("expansion_population").build();
    public static final ItemStack expansionBuy = ItemBuilder.loading().id("expansion_buy").build();

    public static final ItemStack none = new ItemBuilder(Material.BARRIER, Msg.format("None item"), "none").build();
    public static final ItemStack wheat = new ItemBuilder(Material.WHEAT, Msg.format("<white>Wheat"), "wheat").build();
    public static final ItemStack corn = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Corn"), "corn").build();
    public static final ItemStack carrot = new ItemBuilder(Material.CARROT, Msg.format("<white>Carrot"), "carrot").build();
    public static final ItemStack sugarcane = new ItemBuilder(Material.SUGAR_CANE, Msg.format("<white>Sugarcane"), "sugarcane").build();
    public static final ItemStack bread = new ItemBuilder(Material.BREAD, Msg.format("<white>Bread"), "bread").build();
    public static final ItemStack cookie = new ItemBuilder(Material.COOKIE, Msg.format("<white>Cookie"), "cookie").build();
    public static final ItemStack bagel = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Bagel"), "bagel").build();
    public static final ItemStack cream = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Cream"), "cream").build();
    public static final ItemStack cheese = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Cheese"), "cheese").build();
    public static final ItemStack sugar = new ItemBuilder(Material.SUGAR, Msg.format("<white>Sugar"), "sugar").build();
    public static final ItemStack paint = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Paint bucket"), "paint").build();
    public static final ItemStack hammer = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Hammer"), "hammer").build();
    public static final ItemStack nail = new ItemBuilder(Material.IRON_BARS, Msg.format("<white>Nail"), "nail").build();
    //barn
    public static final ItemStack barnSell = ItemBuilder.loading().id("barn_sell").build();
    public static final ItemStack barnIncreaseAmount = ItemBuilder.loading().id("barn_increase").build();
    public static final ItemStack barnDecreaseAmount = ItemBuilder.loading().id("barn_decrease").build();
    public static final ItemStack barnUpgrade = ItemBuilder.loading().id("barn_upgrade").build();
    public static final ItemStack barnStorage = ItemBuilder.loading().id("barn_storage").build();
    //trains
    public static final ItemStack trainEngine = ItemBuilder.loading().id("train_engine").build();
    public static final ItemStack trainCar = ItemBuilder.loading().id("train_car").build();

    public static class World {
        public static final ItemStack grass = new ItemBuilder(Material.GRASS_BLOCK, Msg.format(""), "grass").build();
        public static final ItemStack stone = new ItemBuilder(Material.STONE, Msg.format(""), "stone").build();
        public static final ItemStack sand = new ItemBuilder(Material.SAND, Msg.format(""), "sand").build();
        public static final ItemStack water = new ItemBuilder(Material.WATER_BUCKET, Msg.format(""), "water").build();
        public static final ItemStack road = new ItemBuilder(Material.BLACK_CONCRETE, Msg.format(""), "road").build();
        public static final ItemStack wood_plank = new ItemBuilder(Material.OAK_PLANKS, Msg.format(""), "wood_plank").build();
        public static final ItemStack expansion = new ItemBuilder(Material.PODZOL, Msg.format("Expansion"), "expansion").build();
        public static final ItemStack plot = new ItemBuilder(Material.FARMLAND, Msg.format("Empty Plot"), List.of(Msg.format("<green>You should plant something!")), "plot_none").build();
        public static final ItemStack barn = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<blue>Barn"), "barn")
                .setSkullTextureURL("https://textures.minecraft.net/texture/4a9953ce945da6e2fb80030e754fa8e092c4e9ecd0548f938c3497a808e41a48").build();
        public static final ItemStack train = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("Trains"), "train").build();
        //animals
        public static final ItemStack cowshed = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Cowshed"), "cowshed").build();
        public static final ItemStack chicken_coop = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Chicken Coop"), "chicken_coop").build();
        //factories
        public static final ItemStack bakery = new ItemBuilder(Material.BREAD, Msg.format("<white>Bakery"), "bakery").build();
        public static final ItemStack feed_mill = new ItemBuilder(Material.WHEAT, Msg.format("<white>Feed Mill"), "feed_mill").build();
        public static final ItemStack dairy = new ItemBuilder(Material.MILK_BUCKET, Msg.format("<white>Dairy Factory"), "dairy_factory").build();
        public static final ItemStack sugar = new ItemBuilder(Material.SUGAR, Msg.format("<white>Sugar Factory"), "sugar_factory").build();
    }

    public static class Recipes {
        //bakery
        public static final ItemStack bread = new ItemBuilder(Material.BREAD, Msg.format("<white>Bread"), List.of(Msg.format("<white>2 wheat")), "bread_recipe").build();
        public static final ItemStack cookie = new ItemBuilder(Material.COOKIE, Msg.format("<white>Cookie"), List.of(Msg.format("<white>2 wheat"), Msg.format("<white>2 eggs")), "cookie_recipe").build();
        public static final ItemStack bagel = new ItemBuilder(Material.PLAYER_HEAD, Msg.format("<white>Bagel"), List.of(Msg.format("<white>2 wheat"), Msg.format("<white>1 sugar"), Msg.format("<white>3 eggs")), "bagel_recipe").build();
        //the feed mill
        public static final ItemStack cowFeed = new ItemBuilder(Material.WHEAT, Msg.format("<white>Cow Feed"), List.of(Msg.format("<white>2 wheat"), Msg.format("<white>1 corn")), "cow_feed_recipe").build();
        public static final ItemStack chickenFeed = new ItemBuilder(Material.EGG, Msg.format("<white>Chicken Feed"), List.of(Msg.format("<white>2 wheat"), Msg.format("<white>1 carrots")), "chicken_feed_recipe").build();
        //dairy
        public static final ItemStack cream = new ItemBuilder(Material.MAGMA_CREAM, Msg.format("<white>Cream"), List.of(Msg.format("<white>1 milk")), "cream_recipe").build();
        public static final ItemStack cheese = new ItemBuilder(Material.GOLD_NUGGET, Msg.format("<white>Cheese"), List.of(Msg.format("<white>2 milk")), "cheese_recipe").build();
        //sugar
        public static final ItemStack sugar = new ItemBuilder(Material.SUGAR, Msg.format("<white>Sugar"), List.of(Msg.format("<white>1 sugarcane")), "sugar_recipe").build();
    }
}