     package me.webhead1104.township.listeners;
     
     import java.sql.PreparedStatement;
     import java.sql.ResultSet;
     import java.sql.SQLException;
     import java.util.EnumSet;
     import java.util.Objects;
     import me.flame.menus.menu.Menu;
     import me.flame.menus.modifiers.Modifier;
     import me.webhead1104.township.data.Database;
     import me.webhead1104.township.utils.MenuItems;
     import me.webhead1104.township.utils.Utils;
     import org.bukkit.ChatColor;
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
                 PreparedStatement preparedStatement = Database.getConnection().prepareStatement("SELECT * FROM Township WHERE PlayerUUID = ?");
                 preparedStatement.setString(1, event.getPlayer().getUniqueId().toString());
                 ResultSet resultSet = preparedStatement.executeQuery();
                 if (resultSet.next())
                     if (Objects.equals(resultSet.getString("PlayerUUID"), event.getPlayer().getUniqueId().toString())) {
                         // Utils.mainMenu(event.getPlayer());
                     } else Database.newPlayer(event.getPlayer());
             } catch (Exception e){}
         }
     }