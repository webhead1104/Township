     package me.webhead1104.township.listeners;
     
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.sql.SQLException;
     import java.util.Objects;
     import me.webhead1104.township.data.Database;
     import org.bukkit.event.EventHandler;
     import org.bukkit.event.Listener;
     import org.bukkit.event.player.PlayerJoinEvent;
     import me.webhead1104.township.Township;
     
     public class JoinListener implements Listener {

         Township plugin;
         public JoinListener(Township plugin) {
             this.plugin = plugin;
         }

         @EventHandler
         public void onJoin(PlayerJoinEvent event) {
             try {
                 event.getPlayer().getInventory().clear();
                 Database.connect();
                 PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM TownshipPlayerData WHERE PlayerUUID = ?");
                 preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
                 ResultSet resultSet = preparedStatement.executeQuery();
                 resultSet.next();
                 if (Objects.equals(resultSet.getString("PlayerUUID"), event.getPlayer().getUniqueId().toString())) {
                     plugin.mainMenu(event.getPlayer());
                 }
             } catch (SQLException e) {
                 Database.newPlayer(event.getPlayer());
             }
         }
     }