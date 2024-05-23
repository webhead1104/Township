package me.webhead1104.township.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.webhead1104.township.Township;
import me.webhead1104.township.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;

public class Database {
    public static boolean sqlite = false;
    private static String host;
    private static String port;
    private static String databaseName;
    private static String username;
    private static String password;
    private static Connection connection;
    public Database() {
        host = Township.INSTANCE.getConfig().getString("mysql.host");
        port = Township.INSTANCE.getConfig().getString("mysql.port");
        databaseName = Township.INSTANCE.getConfig().getString("mysql.databaseName");
        username = Township.INSTANCE.getConfig().getString("mysql.username");
        password = Township.INSTANCE.getConfig().getString("mysql.password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));
        }
    }

    public static boolean isConnected() {
        return (connection != null);
    }

    public static void connect() {
        try {
            if (!isConnected())
                if (Objects.equals(Township.INSTANCE.getConfig().getString("databaseType"), "sqlite")) {
                    sqlite = true;
                    Sqlite sqlite = new Sqlite();
                    connection = sqlite.connection;
                } else if (Objects.equals(Township.INSTANCE.getConfig().getString("databaseType"), "mysql")) {
                    sqlite = false;
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:"+"mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false&autoReconnect=true,"+ username+","+password);
                    Township.INSTANCE.getLogger().log(Level.INFO,"MySQL loaded!");
                }

        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR in connecting " + e +" "+ Arrays.toString(e.getStackTrace()));
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed())
                connection.close();
        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));
        }
    }

    public static void create() {
        if (sqlite) return;
        if (isConnected()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Township");
                preparedStatement.executeQuery();
            } catch (SQLException e) {
                Township.INSTANCE.getLogger().log(Level.INFO, "Creating table!");
                createTable();
            }
        }
    }

    public static Connection getConnection() {return connection;}

    public static void createTable() {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS Township(PlayerUUID VARCHAR NOT NULL PRIMARY KEY,
                    data TEXT NOT NULL""");
            statement.executeUpdate();
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));}
    }

    public static void resetTable(CommandSender player) {
        try {
            createTable();
            PreparedStatement thing = connection.prepareStatement("DROP TABLE Township;");
            thing.executeUpdate();
            createTable();
            player.sendMessage(ChatColor.GREEN + "Database reset complete");
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));}
    }

    public static JsonObject getData(UUID uuid) {
        JsonObject output = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Township WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            output = new Gson().fromJson(res.getString("data"), JsonObject.class);
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));}
        return output;
    }

    public static void setData(UUID uuid, String value) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE Township SET data = ? WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, uuid.toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));}
    }

    public static String getItem(UUID uuid ,String item) {
        String output = null;
        try {
            output = getData(uuid).get("items").getAsJsonObject().get(item).getAsString();
        }catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));}
        return output;
    }

    public static void setItem(UUID uuid, String item, String value) {
        try {
            JsonObject obj = new Gson().fromJson(getData(uuid), JsonObject.class).get("items").getAsJsonObject();
            obj.addProperty(item,value);
            setData(uuid,obj.toString());
        }catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e +" "+ Arrays.toString(e.getStackTrace()));}}

    public static void newPlayer(Player player) {
        try {
            JsonObject obj = Township.dataTable;
            obj.addProperty("PlayerUUID",player.getUniqueId().toString());
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Township (PlayerUUID, data)VALUES (?,?);");
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2,obj.toString());
            ps.execute();
            //Utils.mainMenu(player);
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");}
    }
}