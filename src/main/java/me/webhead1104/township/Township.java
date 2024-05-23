      package me.webhead1104.township;

      import com.google.gson.Gson;
      import com.google.gson.JsonObject;
      import org.bukkit.ChatColor;
      import org.bukkit.plugin.java.JavaPlugin;
      import me.webhead1104.township.data.Database;
      import me.webhead1104.township.listeners.InventoryClickListener;
      import me.webhead1104.township.listeners.JoinListener;
      import java.io.File;
      import java.io.IOException;
      import java.nio.file.Files;
      import java.nio.file.Paths;
      import java.util.*;
      import java.util.logging.Level;

      public class Township extends JavaPlugin {

          public static Township INSTANCE;
          public static JsonObject dataTable;
          Factories factories1;
          WorldManager worldManager1;
          LevelMenu levelMenu;
          Animals animals;
          public void onEnable() {
              INSTANCE = this;
              File file = new File(getDataFolder().getAbsolutePath() + "/config.yml");
              if (!file.exists())
                  saveResource("config.yml", false);
              factories1 = new Factories();
              worldManager1 = new WorldManager();
              levelMenu = new LevelMenu();
              animals = new Animals();
              try {
                  saveResource("database.json", true);
                  dataTable = new Gson().fromJson(new String(
                          Files.readAllBytes(Paths.get(getDataFolder().getAbsolutePath()+ "/database.json"))), JsonObject.class);
                  //noinspection ResultOfMethodCallIgnored
                  new File(getDataFolder().getAbsolutePath() + "/database.json").delete();
              } catch (IOException e) {
                  throw new RuntimeException(e);
              }
              Objects.requireNonNull(getCommand("township")).setExecutor(new Command(this));
              registerListeners();
              Database.connect();
              if (Database.isConnected()) Database.create();
              if (Database.isConnected()) getLogger().log(Level.INFO, ChatColor.GREEN + "Database loaded!");
          }

          public void onDisable() {
              Database.closeConnection();
          }

          public void registerListeners() {
              getServer().getPluginManager().registerEvents(new JoinListener(this), this);
              getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
          }

          public Animals getAnimals() {return animals;}
          public Factories getFactories() {return factories1;}
          public WorldManager getWorldManager() {return worldManager1;}
          public LevelMenu getLevelMenu() {return levelMenu;}
      }