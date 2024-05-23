      package me.webhead1104.township;

      import java.util.ArrayList;
      import java.util.EnumSet;
      import java.util.List;
      import me.flame.menus.menu.Menu;
      import me.flame.menus.modifiers.Modifier;
      import me.webhead1104.township.data.Database;
      import me.webhead1104.township.utils.MenuItems;
      import me.webhead1104.township.utils.Utils;
      import org.bukkit.ChatColor;
      import org.bukkit.command.CommandExecutor;
      import org.bukkit.command.CommandSender;
      import org.bukkit.command.TabCompleter;
      import org.bukkit.entity.Player;
      import org.jetbrains.annotations.NotNull;
      import org.jetbrains.annotations.Nullable;

      public class Command implements CommandExecutor, TabCompleter {

          Township plugin;

          public Command(Township plugin) {
              this.plugin = plugin;
          }

          public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
              if (args.length == 0) {
                  if (sender instanceof Player player) {
                      Menu mainMenu = Menu.create(ChatColor.GOLD + "Main Menu", 5, EnumSet.allOf(Modifier.class));
                      player.getInventory().clear();
                      mainMenu.getFiller().fill(MenuItems.glass);
                      mainMenu.setItem(22, MenuItems.township);
                      mainMenu.open(player);
                  }
              } else {
                  switch (args[0].toLowerCase()) {
                      case "animals" -> {
                          if (sender instanceof Player player) {
                              Menu menu = Menu.create("animals", 6);
                              menu.setItem(0, MenuItems.cowshedMenuItem);
                              menu.setItem(1, MenuItems.chickenMenuItem);
                              menu.open(player);
                          }
                      }
                      case "factories" -> {
                          if (sender instanceof Player player) {
                              Menu menu = Menu.create("test", 6);
                              menu.addItem(MenuItems.bakeryMenuItem);
                              menu.addItem(MenuItems.feedmillMenuItem);
                              menu.addItem(MenuItems.dairyMenuItem);
                              menu.addItem(MenuItems.sugarMenuItem);
                              menu.open(player);
                          }
                      }
                      case "table" -> Database.resetTable(sender);
                      case "reload" -> {
                          plugin.reloadConfig();
                          Database.create();
                          sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
                      }
                  }
              }
              return true;
          }

          @Nullable
          public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String s, @NotNull String[] args) {
              List<String> returnme = new ArrayList<>();
              if (sender.hasPermission("township.admin")) {
                  if (args.length == 1) {
                      returnme.addAll(List.of("table", "animals", "reload", "factories"));
                  }
              }
              return returnme;
          }
      }