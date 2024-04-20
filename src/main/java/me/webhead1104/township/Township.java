      package me.webhead1104.township;

      import me.flame.menus.menu.PaginatedMenu;
      import me.flame.menus.modifiers.Modifier;
      import me.webhead1104.township.utils.Items;
      import org.bukkit.ChatColor;
      import org.bukkit.entity.Player;
      import org.bukkit.plugin.java.JavaPlugin;
      import me.webhead1104.township.data.Database;
      import me.webhead1104.township.listeners.InventoryClickListener;
      import me.webhead1104.township.listeners.JoinListener;
      import java.io.File;
      import java.util.*;
      import java.util.logging.Level;

      public class Township extends JavaPlugin {

          Factories factories1;
          WorldManager worldManager1;
          LevelMenu levelMenu;
          Animals animals;
          PaginatedMenu mainMenu;
          public void onEnable() {
              mainMenu = PaginatedMenu.create(ChatColor.GOLD + "Main Menu", 5, 2, EnumSet.allOf(Modifier.class));
              File file = new File("plugins/Township/config.yml");
              if (!file.exists())
                  this.saveResource("config.yml", false);
              factories1 = new Factories(this);
              worldManager1 = new WorldManager(this);
              levelMenu = new LevelMenu(this);
              animals = new Animals(this);
              new Items(this);
              new Database(this);
              Objects.requireNonNull(getCommand("township")).setExecutor(new Command(this));
              registerListeners();
              Database.connect();
              if (Database.isConnected()) Database.create();
              if (Database.isConnected()) this.getLogger().log(Level.INFO, ChatColor.GREEN + "Database loaded!");
          }

          public void onDisable() {
              Database.disconnect();
          }

          public void registerListeners() {
              getServer().getPluginManager().registerEvents(new JoinListener(this), this);
              getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
          }

          public void mainMenu(Player player) {
              player.getInventory().clear();
              mainMenu.getFiller().fill(Items.glass);
              mainMenu.setItem(22, Items.township);
              mainMenu.open(player);
          }

          public Animals getAnimals() {return animals;}
          public Factories getFactories() {return factories1;}
          public WorldManager getWorldManager() {return worldManager1;}
          public LevelMenu getLevelMenu() {return levelMenu;}
      }