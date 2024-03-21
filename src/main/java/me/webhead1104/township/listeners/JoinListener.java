     package me.webhead1104.township.listeners;
     
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.sql.SQLException;
     import java.util.Objects;
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
                 plugin.getDatabase().connect();
                 PreparedStatement preparedStatement = plugin.getDatabase().getConnection().prepareStatement("SELECT * FROM TownshipPlayerData WHERE PlayerUUID = ?");
                 preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
                 ResultSet resultSet = preparedStatement.executeQuery();
                 resultSet.next();
                 if (Objects.equals(resultSet.getString("PlayerUUID"), event.getPlayer().getUniqueId().toString())) {
                     plugin.getCommand().mainMenu(event.getPlayer());
                 }
             } catch (SQLException e) {
                 plugin.getDatabase().newPlayer(event.getPlayer());
             }
         }
     }