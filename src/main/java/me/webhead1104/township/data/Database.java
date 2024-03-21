package me.webhead1104.township.data;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import me.webhead1104.township.Township;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;

public class Database {

    private final Township plugin;
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;
    private Connection connection;
    public Database(Township plugin) {
        this.plugin = plugin;
        this.host = plugin.getConfig().getString("mysql.host");
        this.port = plugin.getConfig().getString("mysql.port");
        this.database = plugin.getConfig().getString("mysql.databaseName");
        this.username = plugin.getConfig().getString("mysql.username");
        this.password = plugin.getConfig().getString("mysql.password");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch(ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load database driver");
            plugin.getLogger().log(Level.SEVERE,""+e);
        }
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() {
        try {
            if (!isConnected()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&autoReconnect=true", username, password);
                plugin.getLogger().log(Level.INFO,"Creating tables");
                createTablePlayerData();
                createTableWorld();
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE,"DATABASE ERROR " +e);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().log(Level.SEVERE, "An error occoured whilst disconnecting from the database. Please report the following stacktrace to Webhead1104: \n" + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTableWorld() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS TownshipWorldData(PlayerUUID VARCHAR(255)," +
                    "plotpage TEXT DEFAULT ('plot1 0 plot2 0 plot3 0 plot4 0 plot5 0 plot6 0 plot7 0 plot8 0 plot9 0 plot10 0 end')," +
                    "plotslot TEXT DEFAULT ('plot1 0 plot2 0 plot3 0 plot4 0 plot5 0 plot6 0 plot7 0 plot8 0 plot9 0 plot10 0 end')," +
                    "plottype TEXT DEFAULT ('plot1 0 plot2 0 plot3 0 plot4 0 plot5 0 plot6 0 plot7 0 plot8 0 plot9 0 plot10 0 end')," +
                    "factorypage TEXT DEFAULT ('feedmill 0 bakery 0 dairy 0 sugar 0 end')," +
                    "factoryslot TEXT DEFAULT ('feedmill 0 bakery 0 dairy 0 sugar 0 end')," +
                    "animalspage TEXT DEFAULT ('cowshed 0 chickencoop 0 sheepfarm 0 pigfarm 0 end')," +
                    "animalsslot TEXT DEFAULT ('cowshed 0 chickencoop 0 sheepfarm 0 pigfarm 0 end')," +
                    "lastpage INT DEFAULT 0," +
                    "PRIMARY KEY(PlayerUUID))");
            statement.executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE,"ERROR " +e);
        }
    }

    public void createTablePlayerData() {
        try {
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS TownshipPlayerData(PlayerUUID VARCHAR(255), townName TEXT DEFAULT ('none'), level INT  DEFAULT 0, population INT  DEFAULT 0, coins INT  DEFAULT 1000, cash INT  DEFAULT 0, " +
                    "MILK INT  DEFAULT 0, EGGS INT  DEFAULT 0, WOOL INT  DEFAULT 0, BACON INT  DEFAULT 0, BREAD INT  DEFAULT 0, COOKIES INT  DEFAULT 0, BAGEL INT  DEFAULT 0, cowfeed INT  DEFAULT 0, chickenfeed INT  DEFAULT 0, sheepfeed INT  DEFAULT 0, " +
                    "cowshedmilk TEXT DEFAULT ('one 0 two 0 three 0 four 0 five 0 end'), " +
                    "cowshedfeed TEXT DEFAULT ('one 0 two 0 three 0 four 0 five 0 end'), " +
                    "chickencoopegg TEXT DEFAULT ('one 0 two 0 three 0 four 0 five 0 end'), " +
                    "chickencoopfeed TEXT DEFAULT ('one 0 two 0 three 0 four 0 five 0 end')," +
                    "sheepfarmwool TEXT DEFAULT ('one 0 two 0 three 0 four 0 five 0 end'), " +
                    "sheepfarmfeed TEXT DEFAULT ('one 0 two 0 three 0 four 0 five 0 end')," +
                    "lastpage INT DEFAULT 0," +
                    "PRIMARY KEY(PlayerUUID))");
            statement.executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE,"ERROR " +e);
        }
    }

    public void resetTable(CommandSender player) {
        try {
            connect();
            createTableWorld();
            createTablePlayerData();
            PreparedStatement thing = connection.prepareStatement("DROP TABLE TownshipPlayerData,TownshipWorldData;");
            thing.executeUpdate();
            createTableWorld();
            createTablePlayerData();
            player.sendMessage(ChatColor.GREEN + "Database reset complete");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
    }

    public String getWorldData(Player player, String input) {
        this.connect();
        String output = null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM TownshipWorldData WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            output = res.getString(input);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
        return output;
    }

    public String getPlayerData(Player player, String input) {
        this.connect();
        String output = null;
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("SELECT * FROM TownshipPlayerData WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            output = res.getString(input);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
        return output;
    }


    public void setPlayerData(Player player, String column, String value) {
        this.connect();
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("UPDATE TownshipPlayerData SET "+column+" = ? WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
    }

    public void setWorldata(Player player, String column, String value) {
        this.connect();
        try {
            PreparedStatement preparedStatement = this.getConnection().prepareStatement("UPDATE TownshipWorldData SET "+column+" = ? WHERE PlayerUUID = ?;");
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
    }
}