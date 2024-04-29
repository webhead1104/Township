      package me.webhead1104.township;

      import java.util.ArrayList;
      import java.util.List;
      import me.flame.menus.menu.Menu;
      import me.webhead1104.township.data.Database;
      import me.webhead1104.township.utils.Items;
      import org.bukkit.ChatColor;
      import org.bukkit.command.CommandExecutor;
      import org.bukkit.command.CommandSender;
      import org.bukkit.command.TabCompleter;
      import org.bukkit.entity.Player;
      import org.jetbrains.annotations.NotNull;
      import org.jetbrains.annotations.Nullable;

      public class Command implements CommandExecutor, TabCompleter {

          Township plugin;
          public Command(Township plugin) {this.plugin = plugin;}
          public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, String[] args) {
              if (args.length == 0) {
                  if (sender instanceof Player player) plugin.mainMenu(player);
              } else {
                  switch (args[0].toLowerCase()) {
                      case "animals" -> {
                          if (sender instanceof Player player) {
                              Menu menu = Menu.create("animals", 6);
                              menu.setItem(0, Items.cowshedMenuItem);
                              menu.setItem(1, Items.chickenMenuItem);
                              menu.open(player);
                          }
                      }
                      case "factories" -> {
                          if (sender instanceof Player player) {
                              Menu menu = Menu.create("test", 6);
                              menu.addItem(Items.bakeryMenuItem);
                              menu.addItem(Items.feedmillMenuItem);
                              menu.addItem(Items.dairyMenuItem);
                              menu.addItem(Items.sugarMenuItem);
                              menu.open(player);
                          }
                      }
                      case "table" -> Database.resetTable(sender);
                      case "reload" -> {
                          plugin.reloadConfig();
                          Database.connect();
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
                      returnme.addAll(List.of("table", "animals", "reload","factories"));
                  }
              }
              return returnme;
          }
      }