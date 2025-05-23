package me.webhead1104.township.data;

import lombok.Getter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SuppressWarnings("UnusedReturnValue")
public class Database {
    @Getter
    private static Connection sqlConnection;
    private final Township plugin;
    private final ExecutorService worker;
    private final String mysqlHost;
    private final String mysqlPort;
    private final String mysqlDatabase;
    private final String mysqlUsername;
    private final String mysqlPassword;
    private final boolean useSSL;

    public Database(Township plugin) {
        this.plugin = plugin;
        this.worker = Executors.newSingleThreadExecutor(Thread.ofVirtual().name("TownshipDatabaseWorker")
                .uncaughtExceptionHandler((t, e) -> Township.logger.error("An uncaught exception occurred on the thread: {}", t.getName(), e)).factory());
        this.mysqlHost = plugin.getConfig().getString("mysql.host");
        this.mysqlPort = plugin.getConfig().getString("mysql.port");
        this.mysqlDatabase = plugin.getConfig().getString("mysql.database");
        this.mysqlUsername = plugin.getConfig().getString("mysql.username");
        this.mysqlPassword = plugin.getConfig().getString("mysql.password");
        this.useSSL = plugin.getConfig().getBoolean("mysql.use_ssl");
    }

    public boolean isConnected() {
        return (sqlConnection != null);
    }

    public void connect() {
        worker.submit(() -> {
            if (!isConnected()) {
                String databaseType = "";
                try {
                    databaseType = plugin.getConfig().getString("databaseType");
                } catch (Exception e) {
                    Township.logger.error("error", e);
                }
                if (databaseType == null) {
                    Township.logger.error("No databaseType specified in config.yml!");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    return;
                }
                if (databaseType.equalsIgnoreCase("mysql")) {
                    try {
                        sqlConnection = DriverManager.getConnection("jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDatabase + "?useSSL=" + useSSL + "&autoReconnect=true&allowPublicKeyRetrieval=true", mysqlUsername, mysqlPassword);
                        Township.logger.info("Successfully connected to the MySQL Database!");
                    } catch (Exception e) {
                        Township.logger.info("An error occurred whilst connecting to the database.\nCheck your credentials!\nPlease report the following stacktrace to Webhead1104: ", e);
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    }
                } else if (databaseType.equalsIgnoreCase("sqlite")) {
                    try {
                        sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder().getAbsolutePath() + "/township.db");
                        Township.logger.info("Successfully connected to the SQLite Database!");
                    } catch (Exception e) {
                        Township.logger.info("An error occurred whilst connecting to the database.\nCheck the file path!\nPlease report the following stacktrace to Webhead1104: ", e);
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    }
                } else {
                    Township.logger.error("Unknown databaseType specified in config.yml!");
                    Bukkit.getPluginManager().disablePlugin(plugin);
                }
            }
        });
    }

    public void disconnect() {
        worker.submit(() -> {
            if (isConnected()) {
                try {
                    sqlConnection.close();
                    Township.logger.info("Database connection closed!");
                } catch (SQLException e) {
                    Township.logger.error("An error occurred whilst disconnecting from the database. Please report the following stacktrace to Webhead1104: ", e);
                }
            }
        });
    }

    public void createTownshipTable() {
        worker.submit(() -> {
            if (isConnected()) {
                PreparedStatement ps;
                try {
                    ps = sqlConnection.prepareStatement("CREATE TABLE IF NOT EXISTS Township(PlayerUUID VARCHAR(36) PRIMARY KEY, data JSON);");
                    ps.executeUpdate();
                } catch (SQLException e) {
                    Township.logger.error("An error occurred whilst creating the table. Please report the following stacktrace to Webhead1104:", e);
                }
            }
        });
    }

    @NotNull
    public CompletableFuture<Void> setData(@NotNull User user) {
        Validate.notNull(user);
        CompletableFuture<Void> future = new CompletableFuture<>();
        worker.submit(() -> {
            if (!isConnected())
                throw new IllegalStateException("The database must be connected to set a player's data!");
            try {
                long start = System.currentTimeMillis();
                PreparedStatement ps = sqlConnection.prepareStatement("UPDATE Township SET data = ? WHERE PlayerUUID = ?;");
                ps.setString(1, user.toString());
                ps.setString(2, user.getUuid().toString());
                ps.executeUpdate();
                Township.logger.info("User saved in {} mills!", System.currentTimeMillis() - start);
                future.complete(null);
            } catch (SQLException e) {
                Township.logger.error("An error occurred whilst setting a player's data! Please report the following stacktrace to Webhead1104:", e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public CompletableFuture<User> getUser(UUID uuid) {
        CompletableFuture<User> future = new CompletableFuture<>();
        worker.submit(() -> {
            if (!isConnected())
                throw new IllegalStateException("The database must be connected to get a player!");
            try {
                long start = System.currentTimeMillis();
                PreparedStatement ps = sqlConnection.prepareStatement("SELECT * FROM Township WHERE PlayerUUID = ?;");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Township.logger.info("User loaded! in {} mills!", System.currentTimeMillis() - start);
                    future.complete(User.fromJson(rs.getString("data")));
                } else {
                    future.complete(null);
                }
            } catch (SQLException e) {
                Township.logger.error("An error occurred whilst getting a player's data! Please report the following stacktrace to Webhead1104:", e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public CompletableFuture<Boolean> hasPlayerJoinedBefore(UUID uuid) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        worker.submit(() -> {
            if (!isConnected())
                throw new IllegalStateException("The database must be connected to check if a player has joined before!");
            try {
                PreparedStatement ps = sqlConnection.prepareStatement("SELECT * FROM Township WHERE PlayerUUID = ?;");
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();
                future.complete(rs.next());
            } catch (SQLException e) {
                Township.logger.error("An error occurred whilst checking if a player has joined before! Please report the following stacktrace to Webhead1104:", e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @NotNull
    public CompletableFuture<Void> newPlayer(@NotNull UUID uuid) {
        Validate.notNull(uuid);
        CompletableFuture<Void> future = new CompletableFuture<>();
        worker.submit(() -> {
            if (!isConnected())
                throw new IllegalStateException("The database must be connected to add a player!");
            try {
                User user = new User(uuid);
                PreparedStatement ps = sqlConnection.prepareStatement("INSERT INTO Township (PlayerUUID, data) VALUES (?, ?);");
                ps.setString(1, user.getUuid().toString());
                ps.setString(2, user.toString());
                ps.executeUpdate();
                Township.getUserManager().setUser(uuid, user);
                future.complete(null);
            } catch (Exception e) {
                Township.logger.error("An error occurred whilst setting a player's data! Please report the following stacktrace to Webhead1104:", e);
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
