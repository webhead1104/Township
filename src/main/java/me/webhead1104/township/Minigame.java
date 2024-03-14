     package me.webhead1104.township;
     
     import java.util.EnumSet;
     import me.flame.menus.menu.PaginatedMenu;
     import me.flame.menus.modifiers.Modifier;
     import org.bukkit.ChatColor;
     import org.bukkit.Material;
     import org.bukkit.entity.Player;
     import org.bukkit.inventory.Inventory;

     public class Minigame {

         Township plugin;
         public Minigame(Township plugin) {
           this.plugin = plugin;
         }

         public PaginatedMenu levelMenu = PaginatedMenu.create(ChatColor.GOLD + "Levels", 5, 1, EnumSet.of(Modifier.DISABLE_ITEM_REMOVAL, Modifier.DISABLE_ITEM_SWAP, Modifier.DISABLE_ITEM_ADD, Modifier.DISABLE_ITEM_CLONE));
         public PaginatedMenu level1 = PaginatedMenu.create(ChatColor.GOLD + "Level 1", 5, 5, EnumSet.of(Modifier.DISABLE_ITEM_REMOVAL, Modifier.DISABLE_ITEM_SWAP, Modifier.DISABLE_ITEM_ADD, Modifier.DISABLE_ITEM_CLONE));
         public PaginatedMenu level2 = PaginatedMenu.create(ChatColor.GOLD + "Level 2", 5, 5, EnumSet.of(Modifier.DISABLE_ITEM_REMOVAL, Modifier.DISABLE_ITEM_SWAP, Modifier.DISABLE_ITEM_ADD, Modifier.DISABLE_ITEM_CLONE));

         public void setup(Player player) {
             levelMenu.getFiller().fillBorders(Material.WHITE_STAINED_GLASS_PANE);
             levelMenu.setItem(36, plugin.getItems().previousPage);
             levelMenu.setItem(44, plugin.getItems().nextPage);
             levelMenu.setItem(10, plugin.getItems().level1);
             if (player.hasPermission("township.minigame.level.2")) {
                 levelMenu.setItem(11, plugin.getItems().level2);
             } else {
                 plugin.getItems().level2.editor().setName(ChatColor.RED + "Please complete the previous level!").setLore(ChatColor.RED + "Please complete the previous level to play!").done();
             }
             levelMenu.open(player, 0);
         }


         public void level1(Player player) {
             int max = 4, min = 1;
             for (int i = 0; i < 53; i++) {
                 int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
                 if (random == 1) level1.addItem(plugin.getItems().minigameGreen);
                 if (random == 2) level1.addItem(plugin.getItems().minigameBlue);
                 if (random == 3) level1.addItem(plugin.getItems().minigameRed);
                 if (random == 4) level1.addItem(plugin.getItems().minigameYellow);
             }
             levelMenu.open( player, 0);
         }

         public void level2(Player player) {
             int max = 4, min = 1;
             for (int i = 0; i < 53; i++) {
                 int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
                 if (random == 1) level1.addItem(plugin.getItems().minigameGreen);
                 if (random == 2) level1.addItem(plugin.getItems().minigameBlue);
                 if (random == 3) level1.addItem(plugin.getItems().minigameRed);
                 if (random == 4) level1.addItem(plugin.getItems().minigameYellow);
             }
             levelMenu.open(player, 1);
         }


         public void check(Player player, int slot, Inventory inventory) {
             PaginatedMenu level = null;
             int number = 0;
             if (level1.getInventory() == inventory) level = level1;
             if (level2.getInventory() == inventory) level = level2;
             if (level != null) {
                 if (level.getItem(slot - 10) == level.getItem(slot)) number++;
                 if (level.getItem(slot - 9) == level.getItem(slot)) number++;
                 if (level.getItem(slot - 8) == level.getItem(slot)) number++;
                 if (level.getItem(slot - 1) == level.getItem(slot)) number++;
                 if (level.getItem(slot + 1) == level.getItem(slot)) number++;
                 if (level.getItem(slot + 7) == level.getItem(slot)) number++;
                 if (level.getItem(slot + 8) == level.getItem(slot)) number++;
                 if (level.getItem(slot + 9) == level.getItem(slot)) number++;
                 if (number == 8) {
                     level.setItem(slot, plugin.getItems().minigameBomb);
                     level.removePageItem(slot - 10);
                     level.removePageItem(slot - 9);
                     level.removePageItem(slot - 8);
                     level.removePageItem(slot - 1);
                     level.removePageItem(slot + 1);
                     level.removePageItem(slot + 7);
                     level.removePageItem(slot + 8);
                     level.removePageItem(slot + 9);
                     int max = 4, min = 1;
                     for (int i = 0; i < 53; i++) {
                         int random = (int) Math.floor(Math.random() * (max - min + 1) + min);
                         if (random == 1) level.addItem(plugin.getItems().minigameGreen);
                         if (random == 2) level.addItem(plugin.getItems().minigameBlue);
                         if (random == 3) level.addItem(plugin.getItems().minigameRed);
                         if (random == 4) level.addItem(plugin.getItems().minigameYellow);
                         player.sendMessage("YOOOOOOOOOOOOOOOOOOO 8");
                     }
                     player.sendMessage("YOOOOOOOOOOOOOOOOOOO");
                 }
             }
         }
     }