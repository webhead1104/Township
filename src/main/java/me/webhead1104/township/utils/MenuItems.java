     package me.webhead1104.township.utils;

     import me.flame.menus.builders.items.ItemBuilder;
     import me.flame.menus.items.MenuItem;
     import me.webhead1104.township.Township;
     import me.webhead1104.township.data.enums.AnimalType;
     import me.webhead1104.township.data.enums.FactoryType;
     import org.bukkit.ChatColor;
     import org.bukkit.Material;
     import java.util.List;
     import static me.webhead1104.township.data.enums.ItemType.*;

     public class MenuItems {
         
         public MenuItems() {}
         public static MenuItem air = ItemBuilder.of(Material.AIR).buildItem();
         public static MenuItem cow = ItemBuilder.of(Material.COW_SPAWN_EGG).setName(STR."\{ChatColor.GOLD}Cow").buildItem((player, event) -> {});
         public static MenuItem cowshedFeed = ItemBuilder.of(COW_FEED.getItemMaterial()).setName(COW_FEED.getItemName()).setLore(STR."\{ChatColor.GOLD}0").buildItem((player, event) -> Township.INSTANCE.getAnimals().feed(player,AnimalType.COWSHED));
         public static MenuItem milk = ItemBuilder.of(MILK.getItemMaterial()).setName(MILK.getItemName()).setLore(List.of(STR."\{ChatColor.GOLD}1", ChatColor.GREEN + "Click to pick up!")).buildItem((player, event) -> Township.INSTANCE.getAnimals().pickup(player,event));
         public static MenuItem chicken = ItemBuilder.of(Material.CHICKEN_SPAWN_EGG).setName(ChatColor.RED + "Chicken").buildItem((player, event) -> {});
         public static MenuItem chickencoopFeed = ItemBuilder.of(CHICKEN_FEED.getItemMaterial()).setName(CHICKEN_FEED.getItemName()).setLore(STR."\{ChatColor.GOLD}0").buildItem((player, event) -> Township.INSTANCE.getAnimals().feed(player,AnimalType.CHICKEN_COOP));
         public static MenuItem egg = ItemBuilder.of(EGG.getItemMaterial()).setName(EGG.getItemName()).setLore(List.of(STR."\{ChatColor.GOLD}1", STR."\{ChatColor.GREEN}Click to pick up!")).buildItem((player, event) -> Township.INSTANCE.getAnimals().pickup(player,event));
         public static MenuItem arrowUP = ItemBuilder.of(Material.ARROW).setName(STR."\{ChatColor.DARK_GREEN}Click to scroll up!").buildItem();
         public static MenuItem arrowDOWN = ItemBuilder.of(Material.ARROW).setName(STR."\{ChatColor.DARK_GREEN}Click to scroll down!").buildItem();
         public static MenuItem arrowLEFT = ItemBuilder.of(Material.ARROW).setName(STR."\{ChatColor.DARK_GREEN}Click to scroll left!").buildItem();
         public static MenuItem arrowRIGHT = ItemBuilder.of(Material.ARROW).setName(STR."\{ChatColor.DARK_GREEN}Click to scroll right!").buildItem();
         public static MenuItem levelAndPop = ItemBuilder.of(Material.BLUE_CONCRETE).setName(STR."\{ChatColor.AQUA}Level 0").setLore(STR."\{ChatColor.RED}Population 0").buildItem();
         public static MenuItem levelMenuMiddleItem = ItemBuilder.of(Material.BLUE_CONCRETE).setName(STR."\{ChatColor.AQUA}Level null").setLore(STR."\{ChatColor.GRAY}Levels coming soon!").buildItem();
         public static MenuItem coinsAndCash = ItemBuilder.of(Material.GOLD_BLOCK).setName(STR."\{ChatColor.YELLOW}Coins 0").setLore(STR."\{ChatColor.GREEN}Cash 0").buildItem();
         public static MenuItem profile = ItemBuilder.of(Material.LIGHT_BLUE_CONCRETE).setName("null").buildItem();
         public static MenuItem glass = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).setName(" ").buildItem((player, event) -> {});
         public static MenuItem township = ItemBuilder.of(Material.GREEN_CONCRETE).setName(ChatColor.GREEN + "Click to play township!").buildItem((player, event) -> Township.INSTANCE.getWorldManager().load(player));
         public static MenuItem backButton = ItemBuilder.of(Material.BARRIER).setName(ChatColor.RED + "Click to go back!").buildItem((player, event) -> Township.INSTANCE.getWorldManager().getWorld(player));
         public static MenuItem grass = ItemBuilder.of(Material.GRASS_BLOCK).setName(" ").buildItem((player, event) -> {});
         public static MenuItem workingOn = ItemBuilder.of(Material.BLUE_CONCRETE).setName(ChatColor.GREEN+"Loading....").buildItem();
         public static MenuItem completed = ItemBuilder.of(Material.BROWN_CONCRETE).setName(ChatColor.GREEN+"Loading....").buildItem();
         //cowshed
         public static MenuItem cowshedMenuItem = ItemBuilder.of(Material.PLAYER_HEAD).setName(ChatColor.WHITE + "Cowshed").buildItem((player, event) -> Township.INSTANCE.getAnimals().animal(player, AnimalType.COWSHED).open(player));//chicken
         public static MenuItem chickenMenuItem = ItemBuilder.of(Material.PLAYER_HEAD).setName(ChatColor.WHITE + "Chicken Coop").buildItem((player, event) -> Township.INSTANCE.getAnimals().animal(player,AnimalType.CHICKEN_COOP).open(player));
         //bakery
         public static MenuItem bakeryMenuItem = ItemBuilder.of(Material.BREAD).setName(ChatColor.WHITE + "Bakery").buildItem((player, event) -> Township.INSTANCE.getFactories().factory(player, FactoryType.BAKERY));
         public static MenuItem breadRecipe = ItemBuilder.of(Material.BREAD).setName(ChatColor.GOLD + "Bread").setLore(List.of(ChatColor.WHITE+ "2 wheat")).buildItem();
         public static MenuItem cookieRecipe = ItemBuilder.of(Material.BREAD).setName(STR."\{ChatColor.GOLD}Cookie").setLore(List.of(ChatColor.WHITE+ "2 wheat",ChatColor.WHITE+ "2 eggs")).buildItem();
         public static MenuItem bagelRecipe = ItemBuilder.of(Material.BREAD).setName(ChatColor.GOLD + "Bagel").setLore(List.of(ChatColor.WHITE+ "2 wheat",ChatColor.WHITE+ "1 sugar",ChatColor.WHITE+ "3 eggs")).buildItem();
         //feed mill
         public static MenuItem feedmillMenuItem = ItemBuilder.of(Material.WHEAT).setName(ChatColor.WHITE + "Feed Mill").buildItem((player, event) -> Township.INSTANCE.getFactories().factory(player,FactoryType.FEED_MILL));
         public static MenuItem cowfeedrecipe = ItemBuilder.of(Material.WHEAT).setName(ChatColor.GOLD + "Cow Feed").setLore(List.of(ChatColor.WHITE + "2 wheat",ChatColor.WHITE+ "1 corn")).buildItem((player, event) -> player.sendMessage("cow feed"));
         public static MenuItem chickenfeedrecipe = ItemBuilder.of(Material.EGG).setName(ChatColor.GOLD + "Chicken Feed").setLore(List.of(ChatColor.WHITE + "2 wheat", ChatColor.WHITE+"1 carrots")).buildItem((player, event) -> player.sendMessage("chicken feed"));
           //dairy
         public static MenuItem dairyMenuItem = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.WHITE + "Dairy Factory").buildItem((player, event) -> Township.INSTANCE.getFactories().factory(player,FactoryType.DAIRY_FACTORY));
         public static MenuItem creamRecipe = ItemBuilder.of(Material.MAGMA_CREAM).setName(STR."\{ChatColor.GOLD}Cream").setLore(List.of(ChatColor.GOLD + "1 milk")).buildItem();
         public static MenuItem cheeseRecipe = ItemBuilder.of(Material.GOLD_NUGGET).setName(ChatColor.GOLD + "Cheese").setLore(List.of(ChatColor.GOLD + "2 milk")).buildItem();
         public static MenuItem butterRecipe = ItemBuilder.of(Material.GOLD_INGOT).setName(ChatColor.GOLD + "Butter").setLore(List.of(ChatColor.GOLD + "3 milk")).buildItem();
         public static MenuItem yogurtRecipe = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.GOLD + "Yogurt").setLore(List.of(ChatColor.GOLD + "4 milk")).buildItem();
         //sugar
         public static MenuItem sugarMenuItem = ItemBuilder.of(Material.SUGAR).setName(ChatColor.WHITE + "Sugar Factory").buildItem((player, event) -> Township.INSTANCE.getFactories().factory(player,FactoryType.SUGAR_FACTORY));
         public static MenuItem sugarRecipe = ItemBuilder.of(Material.SUGAR).setName(ChatColor.GOLD + "Sugar").setLore(List.of(ChatColor.GOLD + "1 sugarcane")).buildItem();
         public static MenuItem syrupRecipe = ItemBuilder.of(Material.BROWN_CANDLE).setName(ChatColor.GOLD + "Syrup").setLore(List.of(ChatColor.GOLD + "2 sugarcane")).buildItem();
         public static MenuItem caramelRecipe = ItemBuilder.of(Material.BROWN_CONCRETE).setName(ChatColor.GOLD + "Caramel").setLore(List.of(ChatColor.GOLD + "3 sugarcane")).buildItem();
     }