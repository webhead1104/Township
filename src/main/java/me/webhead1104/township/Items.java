     package me.webhead1104.township;

     import me.flame.menus.builders.items.ItemBuilder;
     import me.flame.menus.items.MenuItem;
     import org.bukkit.ChatColor;
     import org.bukkit.Material;
     import org.bukkit.inventory.ItemStack;
     import java.util.List;

     public class Items {

         Township plugin;
         public Items(Township plugin) {
             this.plugin = plugin;
         }

         public MenuItem cow = ItemBuilder.of(Material.COW_SPAWN_EGG).setName(ChatColor.GOLD + "Cow").setLore(ChatColor.GOLD + "Time 0").buildItem((player, event) -> {});
         public MenuItem cowshedFeed = ItemBuilder.of(Material.WHEAT).setName(ChatColor.GOLD + "Cow Feed").setLore(ChatColor.GOLD + "0").buildItem((player, event) -> plugin.getAnimals().cowshedFeed(event));
         public MenuItem cowshedMilk = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.GOLD + "Milk").setLore(List.of(ChatColor.GOLD + "1",ChatColor.GREEN + "Click to pick up!")).buildItem((player, event) -> {plugin.getAnimals().milk(event,player);});

         public MenuItem chicken = ItemBuilder.of(Material.CHICKEN_SPAWN_EGG).setName(ChatColor.RED + "Chicken").setLore(ChatColor.RED + "Really how?!?!?!?!?!?!?!").buildItem((player, event) -> {});
         public MenuItem chickenCoopFeed =  ItemBuilder.of(Material.WHEAT_SEEDS).setName(ChatColor.GOLD + "Chicken Feed").setLore(ChatColor.GOLD + "0").buildItem((player, event) -> {});
         public MenuItem chickenCoopEgg =ItemBuilder.of(Material.EGG).setName(ChatColor.GOLD + "Egg").setLore(List.of(ChatColor.GOLD + "1",ChatColor.GREEN + "Click to pick up!")).buildItem((player, event) -> {});

         public MenuItem sheep = ItemBuilder.of(Material.CHICKEN_SPAWN_EGG).setName(ChatColor.GOLD + "Sheep").setLore(ChatColor.RED + "Really how?!?!?!?!?!?!?!").buildItem((player, event) -> {});
         public MenuItem sheepFarmFeed = ItemBuilder.of(Material.WHEAT_SEEDS).setName(ChatColor.GOLD + "Sheep Feed").setLore(ChatColor.GOLD + "0").buildItem((player, event) -> {});
         public MenuItem sheepFarmWool = ItemBuilder.of(Material.EGG).setName(ChatColor.GOLD + "Wool").setLore(List.of(ChatColor.GOLD + "1",ChatColor.GREEN + "Click to pick up!")).buildItem((player, event) -> {});

         public MenuItem arrowUP = ItemBuilder.of(Material.ARROW).setName(ChatColor.DARK_GREEN + "Click to scroll up!").buildItem((player, event) -> plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()+1));
         public MenuItem arrowDOWN = ItemBuilder.of(Material.ARROW).setName(ChatColor.DARK_GREEN + "Click to scroll down!").buildItem((player, event) -> plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()-1));
         public MenuItem arrowLEFT = ItemBuilder.of(Material.ARROW).setName(ChatColor.DARK_GREEN + "Click to scroll left!").buildItem((player, event) -> plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()+2));
         public MenuItem arrowRIGHT = ItemBuilder.of(Material.ARROW).setName(ChatColor.DARK_GREEN + "Click to scroll right!").buildItem((player, event) -> plugin.getWorldManager().getWorld(player,plugin.getWorldManager().worldMenu.getCurrentPageNumber()-2));
         public MenuItem levelAndPop = ItemBuilder.of(Material.BLUE_CONCRETE).setName(ChatColor.AQUA + "Level 0").setLore(ChatColor.RED + "Population 0").buildItem((player, event) -> (plugin.getCommand()).levelMenu(player));
         public MenuItem coinsAndCash = ItemBuilder.of(Material.GOLD_BLOCK).setName(ChatColor.YELLOW + "Coins 0").setLore(ChatColor.GREEN + "Cash 0").buildItem((player, event) -> {});
         public MenuItem profile = ItemBuilder.of(Material.LIGHT_BLUE_CONCRETE).setName("null").buildItem();
         public MenuItem glass = ItemBuilder.of(Material.WHITE_STAINED_GLASS_PANE).setName(" ").buildItem((player, event) -> {});
         public MenuItem township = ItemBuilder.of(Material.GREEN_CONCRETE).setName(ChatColor.GREEN + "Click to play township!").buildItem((player, event) -> plugin.getCommand().load(player));
         public MenuItem backButton = ItemBuilder.of(Material.BARRIER).setName(ChatColor.RED + "Click to go back!").buildItem((player, event) -> plugin.getWorldManager().getWorld(player,0));
         public MenuItem plantWheat = ItemBuilder.of(Material.WHEAT).setName(ChatColor.GOLD + "Wheat").setLore(List.of("Free","Click to plant wheat!")).buildItem((player, event) -> {player.setItemOnCursor(new ItemStack(Material.WHEAT));});
         public MenuItem plantCorn = ItemBuilder.of(Material.CORNFLOWER).setName(ChatColor.GOLD + "Corn").setLore(List.of("1 coin","Click to plant corn!")).buildItem((player, event) -> {player.setItemOnCursor(new ItemStack(Material.GOLDEN_APPLE));});
         public MenuItem plantCarrot = ItemBuilder.of(Material.CARROT).setName(ChatColor.GOLD + "Carrot").setLore(List.of("2 coins","Click to plant carrots!")).buildItem((player, event) -> {player.setItemOnCursor(new ItemStack(Material.CARROT));});
         public MenuItem plantSugarcane = ItemBuilder.of(Material.SUGAR_CANE).setName(ChatColor.GOLD + "Sugarcane").setLore(List.of("3 coins","Click to plant sugarcane!")).buildItem((player, event) -> {player.setItemOnCursor(new ItemStack(Material.SUGAR_CANE));});

         public MenuItem plot = ItemBuilder.of(Material.PODZOL).setName("plot").buildItem((player, event) -> {
             plugin.getWorldManager().plant(player,event.getSlot(),plugin.getWorldManager().worldMenu.getCurrentPageNumber(),player.getItemOnCursor());
         });
         public MenuItem grass = ItemBuilder.of(Material.GRASS_BLOCK).setName(" ").buildItem((player, event) ->{});

         public MenuItem cowshedTL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Cowshed").buildItem((player, event) -> plugin.getAnimals().cowshed(event));
         public MenuItem cowshedTR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Cowshed").buildItem((player, event) -> plugin.getAnimals().cowshed(event));
         public MenuItem cowshedBL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Cowshed").buildItem((player, event) -> plugin.getAnimals().cowshed(event));
         public MenuItem cowshedBR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Cowshed").buildItem((player, event) -> plugin.getAnimals().cowshed(event));

         public MenuItem chickenTL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Chicken Coop").buildItem((player, event) -> {});
         public MenuItem chickenTR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Chicken Coop").buildItem((player, event) -> {});
         public MenuItem chickenBL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Chicken Coop").buildItem((player, event) -> {});
         public MenuItem chickenBR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Chicken Coop").buildItem((player, event) -> {});

         public MenuItem sheepTL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Sheep Farm").buildItem((player, event) -> {});
         public MenuItem sheepTR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Sheep Farm").buildItem((player, event) -> {});
         public MenuItem sheepBL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Sheep Farm").buildItem((player, event) -> {});
         public MenuItem sheepBR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Sheep Farm").buildItem((player, event) -> {});

         public MenuItem pigTL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Pig Farm").buildItem((player, event) -> {});
         public MenuItem pigTR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Pig Farm").buildItem((player, event) -> {});
         public MenuItem pigBL = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Pig Farm").buildItem((player, event) -> {});
         public MenuItem pigBR = ItemBuilder.of(Material.ACACIA_PRESSURE_PLATE).setName(ChatColor.WHITE + "Pig Farm").buildItem((player, event) -> {});

         public MenuItem completed = ItemBuilder.of(Material.OAK_PLANKS).setName("null").buildItem((player, event) -> player.sendMessage("yay you clicked it!!!!!!!!!!!!!"));
         //feed mill
         public MenuItem feedmillTL = ItemBuilder.of(Material.GOLD_BLOCK).setName(ChatColor.WHITE + "Feed Mill").buildItem((player, event) -> plugin.getFactories().feedmill(player));
         public MenuItem feedmillTR = ItemBuilder.of(Material.GOLD_BLOCK).setName(ChatColor.WHITE + "Feed Mill").buildItem((player, event) -> plugin.getFactories().feedmill(player));
         public MenuItem feedmillBL = ItemBuilder.of(Material.GOLD_BLOCK).setName(ChatColor.WHITE + "Feed Mill").buildItem((player, event) -> plugin.getFactories().feedmill(player));
         public MenuItem feedmillBR = ItemBuilder.of(Material.GOLD_BLOCK).setName(ChatColor.WHITE + "Feed Mill").buildItem((player, event) -> plugin.getFactories().feedmill(player));

         public MenuItem cowfeedrecipe = ItemBuilder.of(Material.WHEAT).setName(ChatColor.GOLD + "Cow Feed").setLore(List.of(ChatColor.GOLD + "2 wheat","1 corn")).buildItem((player, event) -> player.sendMessage("cow feed"));
         public MenuItem chickenfeedrecipe = ItemBuilder.of(Material.EGG).setName(ChatColor.GOLD + "Chicken Feed").setLore(List.of(ChatColor.GOLD + "2 wheat","1 carrots")).buildItem((player, event) -> player.sendMessage("chicken feed"));
         public MenuItem sheepfeedrecipe = ItemBuilder.of(Material.CARROT).setName(ChatColor.GOLD + "Sheep Feed").setLore(List.of(ChatColor.GOLD + "2 corn","2 carrots")).buildItem((player, event) -> player.sendMessage("sheep feed"));
         //bakery
         public MenuItem bakeryTL = ItemBuilder.of(Material.BREAD).setName(ChatColor.WHITE + "Bakery").buildItem((player, event) -> plugin.getFactories().bakery(player));
         public MenuItem bakeryTR = ItemBuilder.of(Material.BREAD).setName(ChatColor.WHITE + "Bakery").buildItem((player, event) -> plugin.getFactories().bakery(player));
         public MenuItem bakeryBL = ItemBuilder.of(Material.BREAD).setName(ChatColor.WHITE + "Bakery").buildItem((player, event) -> plugin.getFactories().bakery(player));
         public MenuItem bakeryBR = ItemBuilder.of(Material.BREAD).setName(ChatColor.WHITE + "Bakery").buildItem((player, event) -> plugin.getFactories().bakery(player));

         public MenuItem breadRecipe = ItemBuilder.of(Material.BREAD).setName(ChatColor.GOLD + "Bread").setLore(List.of(ChatColor.GOLD + "2 wheat")).buildItem();
         public MenuItem cookieRecipe = ItemBuilder.of(Material.BREAD).setName(ChatColor.GOLD + "Cookie").setLore(List.of(ChatColor.GOLD + "2 wheat","2 eggs")).buildItem();
         public MenuItem bagelRecipe = ItemBuilder.of(Material.BREAD).setName(ChatColor.GOLD + "Bread").setLore(List.of(ChatColor.GOLD + "2 wheat","1 sugar","3 eggs")).buildItem();

         public MenuItem dairyTL = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.WHITE + "Dairy Factory").buildItem((player, event) -> plugin.getFactories().dairyFactory(player));
         public MenuItem dairyTR = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.WHITE + "Dairy Factory").buildItem((player, event) -> plugin.getFactories().dairyFactory(player));
         public MenuItem dairyBL = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.WHITE + "Dairy Factory").buildItem((player, event) -> plugin.getFactories().dairyFactory(player));
         public MenuItem dairyBR = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.WHITE + "Dairy Factory").buildItem((player, event) -> plugin.getFactories().dairyFactory(player));

         public MenuItem creamRecipe = ItemBuilder.of(Material.MAGMA_CREAM).setName(ChatColor.GOLD + "Cream").setLore(List.of(ChatColor.GOLD + "1 milk")).buildItem();
         public MenuItem cheeseRecipe = ItemBuilder.of(Material.GOLD_NUGGET).setName(ChatColor.GOLD + "Cheese").setLore(List.of(ChatColor.GOLD + "2 milk")).buildItem();
         public MenuItem butterRecipe = ItemBuilder.of(Material.GOLD_INGOT).setName(ChatColor.GOLD + "Butter").setLore(List.of(ChatColor.GOLD + "3 milk")).buildItem();
         public MenuItem yogurtRecipe = ItemBuilder.of(Material.MILK_BUCKET).setName(ChatColor.GOLD + "Yogurt").setLore(List.of(ChatColor.GOLD + "4 milk")).buildItem();

         public MenuItem sugarTL = ItemBuilder.of(Material.SUGAR).setName(ChatColor.WHITE + "Sugar Factory").buildItem((player, event) -> plugin.getFactories().sugarFactory(player));
         public MenuItem sugarTR = ItemBuilder.of(Material.SUGAR).setName(ChatColor.WHITE + "Sugar Factory").buildItem((player, event) -> plugin.getFactories().sugarFactory(player));
         public MenuItem sugarBL = ItemBuilder.of(Material.SUGAR).setName(ChatColor.WHITE + "Sugar Factory").buildItem((player, event) -> plugin.getFactories().sugarFactory(player));
         public MenuItem sugarBR = ItemBuilder.of(Material.SUGAR).setName(ChatColor.WHITE + "Sugar Factory").buildItem((player, event) -> plugin.getFactories().sugarFactory(player));

         public MenuItem sugarRecipe = ItemBuilder.of(Material.SUGAR).setName(ChatColor.GOLD + "Sugar").setLore(List.of(ChatColor.GOLD + "1 sugarcane")).buildItem();
         public MenuItem syrupRecipe = ItemBuilder.of(Material.BROWN_CANDLE).setName(ChatColor.GOLD + "Syrup").setLore(List.of(ChatColor.GOLD + "2 sugarcane")).buildItem();
         public MenuItem caramelRecipe = ItemBuilder.of(Material.BROWN_CONCRETE).setName(ChatColor.GOLD + "Caramel").setLore(List.of(ChatColor.GOLD + "3 sugarcane")).buildItem();

         //minigame
         public MenuItem nextPage = ItemBuilder.of(Material.ARROW).setName(ChatColor.YELLOW + "Click to go to the next page!").buildItem((i, event) -> {});
         public MenuItem previousPage = ItemBuilder.of(Material.ARROW).setName(ChatColor.YELLOW + "Click to go to the previous page").buildItem((i, event) -> {});
         public MenuItem minigameGreen = ItemBuilder.of(Material.LIME_CONCRETE).setName(" ").buildItem((player, event) -> plugin.getMinigame().check(player,event.getSlot(), event.getInventory()));
         public MenuItem minigameRed = ItemBuilder.of(Material.LIME_CONCRETE).setName(" ").buildItem((player, event) -> plugin.getMinigame().check(player,event.getSlot(), event.getInventory()));
         public MenuItem minigameYellow = ItemBuilder.of(Material.LIME_CONCRETE).setName(" ").buildItem((player, event) -> plugin.getMinigame().check(player,event.getSlot(), event.getInventory()));
         public MenuItem minigameBlue = ItemBuilder.of(Material.LIME_CONCRETE).setName(" ").buildItem((player, event) -> plugin.getMinigame().check(player,event.getSlot(), event.getInventory()));
         public MenuItem minigameBomb = ItemBuilder.of(Material.GUNPOWDER).setName(ChatColor.AQUA + "BOMB powerup").buildItem((player, event) -> {});
         public MenuItem level1 = ItemBuilder.of(Material.LIME_CONCRETE).setName(ChatColor.GREEN + "Click to play level one!").setLore(ChatColor.GREEN + "You can play this level!").buildItem((player, event) -> plugin.getMinigame().level1(player));
         public MenuItem level2 = ItemBuilder.of(Material.LIME_CONCRETE).setName(ChatColor.GREEN + "Click to play level two!").setLore(ChatColor.GREEN + "You can play this level!").buildItem((player, event) -> plugin.getMinigame().level2(player));
     }