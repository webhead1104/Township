package me.webhead1104.township.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import me.webhead1104.township.Township;
import org.bukkit.entity.Player;
import java.sql.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public class Database {

    public static Township plugin;
    private static String host;
    private static String port;
    private static String databaseName;
    private static String username;
    private static String password;
    private static Connection connection;
    public Database(Township plugin) {
        Database.plugin = plugin;
        host = plugin.getConfig().getString("mysql.host");
        port = plugin.getConfig().getString("mysql.port");
        databaseName = plugin.getConfig().getString("mysql.databaseName");
        username = plugin.getConfig().getString("mysql.username");
        password = plugin.getConfig().getString("mysql.password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static boolean isConnected() {
        return (connection != null);
    }

    public static void connect() {
        try {
            if (!isConnected()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String string = "error";
                if (Objects.equals(plugin.getConfig().getString("databaseType"), "mysql")) {
                    string = "mysql://" + host + ":" + port + "/" + databaseName + "?useSSL=false&autoReconnect=true,"+ username+","+password;
                    plugin.getLogger().log(Level.INFO, "mysql loaded");
                } else if (Objects.equals(plugin.getConfig().getString("databaseType"), "sqlite")) {
                    string = "sqlite:plugins/Township/township.db";
                    plugin.getLogger().log(Level.INFO, "sqlite loaded");
                }
                connection = DriverManager.getConnection("jdbc:"+string);
            }
            if (isConnected()) {plugin.getLogger().log(Level.INFO,"enabled");} else plugin.getLogger().log(Level.INFO,"nope");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR in connecting " + Arrays.toString(e.getStackTrace()));
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
        }
    }

    public static void create() {
        if (isConnected()) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM TownshipPlayerData");
                preparedStatement.executeQuery();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.INFO, "Creating tables!");
                createTableWorld();
                createTablePlayerData();
            }
        }
    }

    public static Connection getConnection() {return connection;}

    public static void createTableWorld() {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS TownshipWorldData(PlayerUUID VARCHAR(255),
                    factories JSON DEFAULT ('
                    {
                    "bakery": {
                        "unlocked": "false"
                    	"page": "none",
                    	"slot": "none"
                    	},
                    "feedmill": {
                        "unlocked": "false"
                    	"page": "none",
                    	"slot": "none"
                    	},
                    "dairy_factory": {
                        "unlocked": "false"
                    	"page": "none",
                    	"slot": "none"
                    	},
                    "sugar_factory": {
                        "unlocked": "false"
                    	"page": "none",
                    	"slot": "none"
                    	}
                   }
                    '),
                    animals JSON DEFAULT ('
                    {
                     	"cowshed": {
                     	    "unlocked": "false"
                     		"page": "none",
                     		"slot": "none"
                     	},
                     	"chicken_coop": {
                     	    "unlocked": "false"
                     		"page": "none",
                     		"slot": "none"
                     	}
                     }
                    '),
                    PRIMARY KEY(PlayerUUID))""");
            statement.executeUpdate();
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public static void createTablePlayerData() {
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS TownshipPlayerData(PlayerUUID VARCHAR(255), townName TEXT DEFAULT ('none'),
                    level INT DEFAULT 0, population INT  DEFAULT 0, coins INT  DEFAULT 1000, cash INT  DEFAULT 0,
                    items JSON DEFAULT ('
                    					{
                    						"milk": 0,
                    						"eggs": 0,
                    						"bread": 0,
                    						"coookies": 0,
                    						"bagel": 0,
                    						"cow_feed": 0,
                    						"chicken_feed": 0
                    					}
                    					 '),
                    animals JSON DEFAULT ('{
                                          "cowshed": {
                                          "1": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                          "2": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                         "3": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                          "4": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                          "5": {
                                          "feed": "false",
                                          "product": "false"
                                          }
                                          },
                                        "chicken_coop": {
                                          "1": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                          "2": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                         "3": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                          "4": {
                                          "feed": "false",
                                          "product": "false"
                                          },
                                          "5": {
                                          "feed": "false",
                                          "product": "false"
                                          }
                                          }
                                        }'),
                    factories JSON DEFAULT ('
                    {
                    "bakery": {
                    		"completed": {
                    			"1": "none",
                    			"2": "none",
                    			"3": "none"
                    		},
                    		"working_on": "none"
                    	},
                     "feed_mill": {
                    		"completed": {
                    			"1": "none",
                    			"2": "none",
                    			"3": "none"
                    		},
                    		"working_on": "none"
                    	},
                    "dairy_factory": {
                    		"completed": {
                    			"1": "none",
                    			"2": "none",
                    			"3": "none"
                    		},
                    		"working_on": "none"
                    	},
                    "sugar_factory": {
                    		"completed": {
                    			"1": "none",
                    			"2": "none",
                    			"3": "none"
                    		},
                    		"working_on": "none"
                    	},
                    }
                    '),
                    PRIMARY KEY(PlayerUUID))""");
            statement.executeUpdate();
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public static void resetTable(CommandSender player) {
        try {
            connect();
            createTableWorld();
            createTablePlayerData();
            PreparedStatement thing = connection.prepareStatement("DROP TABLE TownshipPlayerData,TownshipWorldData;");
            thing.executeUpdate();
            createTableWorld();
            createTablePlayerData();
            player.sendMessage(ChatColor.GREEN + "Database reset complete");
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public static String getWorldData(Player player, String input) {
        connect();
        String output = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM TownshipWorldData WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            output = res.getString(input);
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
        return output;
    }

    public static String getPlayerData(Player player, String input) {
        connect();
        String output = null;
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("SELECT * FROM TownshipPlayerData WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            output = res.getString(input);
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
        return output;
    }

    public static void setPlayerData(Player player, String column, String value) {
        connect();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE TownshipPlayerData SET " + column + " = ? WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public static void setWorldata(Player player, String column, String value) {
        connect();
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement("UPDATE TownshipWorldData SET " + column + " = ? WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public static String getItem(Player player,String item) {
        String output = null;
        try {
            String items = getPlayerData(player,"items");
            JsonObject obj = new Gson().fromJson(items, JsonObject.class);
            output = obj.get(item).getAsString();
        }catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
        return output;
    }

    public static void setItem(Player player,String item,String value) {
        try {
            JsonObject obj = new Gson().fromJson(getPlayerData(player,"items"), JsonObject.class);
            obj.addProperty(item.toLowerCase(),value);
            setPlayerData(player,"items",obj.toString());
        }catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}}

    public static void newPlayer(Player player) {
        Database.connect();
        try {
            PreparedStatement playerData = Database.getConnection().prepareStatement("INSERT INTO TownshipPlayerData (PlayerUUID)VALUES (?);");
            playerData.setString(1, player.getUniqueId().toString());
            playerData.executeUpdate();
            PreparedStatement worldData = Database.getConnection().prepareStatement("INSERT INTO TownshipWorldData (PlayerUUID)VALUES (?);");
            worldData.setString(1, player.getUniqueId().toString());
            worldData.executeUpdate();
            plugin.mainMenu(player);
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }
}