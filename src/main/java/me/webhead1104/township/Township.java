      package me.webhead1104.township;

      import java.io.File;
      import java.sql.*;
      import java.util.Objects;
      import java.util.logging.Level;
      import org.bukkit.entity.Player;
      import org.bukkit.plugin.java.JavaPlugin;
      import me.webhead1104.township.data.Database;
      import me.webhead1104.township.listeners.InventoryClickListener;
      import me.webhead1104.township.listeners.InventoryCloseListener;
      import me.webhead1104.township.listeners.JoinListener;
      
      public final class Township extends JavaPlugin {

          Minigame minigame1;
          Items items1;
          Command command1;
          Animals animals1;
          Factories factories1;
          WorldManager worldManager1;
          Database database;

          public void onEnable() {
              File file = new File("plugins/Township/config.yml");
              if (!file.exists())
                  this.saveResource("config.yml", false);
              minigame1 = new Minigame(this);
              items1 = new Items(this);
              command1 = new Command(this);
              animals1 = new Animals(this);
              factories1 = new Factories(this);
              worldManager1 = new WorldManager(this);
              database = new Database(this);
              Objects.requireNonNull(getCommand("township")).setExecutor(new Command(this));
              registerListeners();
              database.connect();
          }

          public void onDisable() {
              database.disconnect();
          }

          public void registerListeners() {
              getServer().getPluginManager().registerEvents(new JoinListener(this), this);
              getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
              getServer().getPluginManager().registerEvents(new InventoryCloseListener(this), this);
          }

          public void newPlayer(Player player) {
              database.connect();
              try {
                  PreparedStatement playerData = database.getConnection().prepareStatement("INSERT INTO TownshipPlayerData (PlayerUUID)VALUE (?);");
                  playerData.setString(1, player.getUniqueId().toString());
                  playerData.executeUpdate();

                  PreparedStatement worldData = database.getConnection().prepareStatement("INSERT INTO TownshipWorldData (PlayerUUID)VALUE (?);");
                  worldData.setString(1, player.getUniqueId().toString());
                  worldData.executeUpdate();

                  this.getCommand().mainMenu(player);
              } catch (Exception e) {
                  this.getLogger().log(Level.SEVERE, e + " ERROR");
              }
          }

          public Items getItems() {
              return items1;
          }

          public Minigame getMinigame() {
              return minigame1;
          }

          public Command getCommand() {
              return command1;
          }

          public Animals getAnimals() {
              return animals1;
          }

          public Factories getFactories() {
              return factories1;
          }

          public WorldManager getWorldManager() {
              return worldManager1;
          }

          public Database getDatabase() {
              return database;
          }
      }