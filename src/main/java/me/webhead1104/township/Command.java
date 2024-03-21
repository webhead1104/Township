      package me.webhead1104.township;

      import java.util.ArrayList;
      import java.util.EnumSet;
      import java.util.List;
      import java.util.logging.Level;
      import me.flame.menus.menu.PaginatedMenu;
      import me.flame.menus.modifiers.Modifier;
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
                      mainMenu(player);
                  }
              } else {
                  switch (args[0].toLowerCase()) {
                      case "cow" -> {
                          if (sender instanceof Player player) {
                              PaginatedMenu testThing = PaginatedMenu.create("cow test", 6, 1);
                              testThing.setItem(0, plugin.getItems().cowshedTL);
                              testThing.setItem(1, plugin.getItems().cowshedTR);
                              testThing.setItem(9, plugin.getItems().cowshedBL);
                              testThing.setItem(10, plugin.getItems().cowshedBR);
                              testThing.open(player);
                          }
                      }
                      case "minigame" -> {
                          if (sender instanceof Player player) {
                              plugin.getMinigame().setup(player);
                          }
                      }
                      case "table" -> {
                          plugin.getDatabase().resetTable(sender);
                      }
                      case "reload" -> {
                          plugin.reloadConfig();
                          plugin.getDatabase().connect();
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
                      returnme.addAll(List.of("minigame", "table", "cow", "chicken", "sheep","reload"));
                  }
              }
              return returnme;
          }

          public PaginatedMenu mainMenu = PaginatedMenu.create(ChatColor.GOLD + "Main Menu", 5, 2, EnumSet.allOf(Modifier.class));
          public PaginatedMenu levelMenu = PaginatedMenu.create(ChatColor.AQUA + "Level Menu", 5, 2, EnumSet.allOf(Modifier.class));
          public PaginatedMenu cowshed = PaginatedMenu.create(ChatColor.GOLD + "Cowshed", 5, 1, EnumSet.allOf(Modifier.class));
          public PaginatedMenu chickencoop = PaginatedMenu.create(ChatColor.GOLD + "Chicken Coop", 5, 1, EnumSet.allOf(Modifier.class));
          public PaginatedMenu sheepfarm = PaginatedMenu.create(ChatColor.GOLD + "Sheep Farm", 5, 1, EnumSet.allOf(Modifier.class));

          public void mainMenu(Player player) {
              player.getInventory().clear();
              mainMenu.setItem(22, plugin.getItems().township);
              mainMenu.getFiller().fill(plugin.getItems().glass);
              mainMenu.open(player);
          }

          public void levelMenu(Player player) {
              player.getInventory().clear();
              levelMenu.setItem(34, plugin.getItems().backButton);
              levelMenu.open(player);
          }

          public void load(Player player) {
              player.sendMessage("load called");
              try {
                  if (plugin.getDatabase().getPlayerData(player,"townName").equals("none")) {
                      plugin.getWorldManager().townName(player);
                  } else {
                      plugin.getWorldManager().getWorld(player, 0);
                  }
                  player.sendMessage("load done");
              } catch (Exception e) {
                  plugin.getLogger().log(Level.SEVERE,"ERROR  in load!!!!! " +e);
              }
          }
      }