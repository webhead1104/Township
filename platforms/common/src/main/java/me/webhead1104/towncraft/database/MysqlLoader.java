/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package me.webhead1104.towncraft.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.exceptions.UnknownUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MysqlLoader extends UpdatableUserLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlLoader.class);
    private static final int CURRENT_DB_VERSION = 1;
    private static final String CREATE_VERSIONING_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `database_version` (`id` INT NOT NULL AUTO_INCREMENT, " +
            "`version` INT(11), PRIMARY KEY(id));";
    private static final String INSERT_VERSION_QUERY = "INSERT INTO `database_version` (`id`, `version`) VALUES (1, ?) ON DUPLICATE KEY UPDATE `id` = ?;";
    private static final String GET_VERSION_QUERY = "SELECT `version` FROM `database_version` WHERE `id` = 1;";

    private static final String RENAME_TABLE_QUERY = "ALTER TABLE `Township` RENAME `users`";
    private static final String RENAME_UUID_QUERY = "ALTER TABLE `users` CHANGE COLUMN `PlayerUUID` `uuid` VARCHAR(36)";

    private static final String CREATE_USERS_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS `users` (`uuid` VARCHAR(36) PRIMARY KEY, `data` JSON);";
    private static final String SELECT_USER_QUERY = "SELECT `data` FROM `users` WHERE `uuid` = ?;";
    private static final String UPDATE_USER_QUERY = "INSERT INTO `users` (`uuid`, `data`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `data` = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM `users` WHERE `uuid` = ?;";
    private static final String LIST_USERS_QUERY = "SELECT `uuid` FROM `users`;";

    private HikariDataSource source;

    public MysqlLoader(String sqlURL, String host, int port, String database, boolean useSSL, String username, String password) {
        try {
            HikariConfig hikariConfig = new HikariConfig();

            sqlURL = sqlURL.replace("{host}", host);
            sqlURL = sqlURL.replace("{port}", String.valueOf(port));
            sqlURL = sqlURL.replace("{database}", database);
            sqlURL = sqlURL.replace("{usessl}", String.valueOf(useSSL));

            hikariConfig.setJdbcUrl(sqlURL);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);

            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
            hikariConfig.addDataSourceProperty("useLocalSessionState", "true");
            hikariConfig.addDataSourceProperty("rewriteBatchedStatements", "true");
            hikariConfig.addDataSourceProperty("cacheResultSetMetadata", "true");
            hikariConfig.addDataSourceProperty("cacheServerConfiguration", "true");
            hikariConfig.addDataSourceProperty("elideSetAutoCommits", "true");
            hikariConfig.addDataSourceProperty("maintainTimeStats", "false");

            source = new HikariDataSource(hikariConfig);
            init();
        } catch (SQLException e) {
            Towncraft.getLogger().error("Failed to establish connection to the MySQL server:", e);
        }
    }

    @Override
    public void update() throws IOException, NewerStorageException {
        try (Connection con = source.getConnection()) {
            int version;

            try (PreparedStatement statement = con.prepareStatement(GET_VERSION_QUERY);
                 ResultSet set = statement.executeQuery()) {
                version = set.next() ? set.getInt(1) : CURRENT_DB_VERSION;
            }

            if (version > CURRENT_DB_VERSION) {
                throw new NewerStorageException(CURRENT_DB_VERSION, version);
            }

            if (version < CURRENT_DB_VERSION) {
                LOGGER.warn("Your MySQL database is outdated. The update process will start in 10 seconds.");
                LOGGER.warn("Note that this update might make your database incompatible with older Towncraft versions.");
                LOGGER.warn("Make sure no other servers with older Towncraft versions are using this database.");
                LOGGER.warn("Shut down the server to prevent your database from being updated.");

                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException ignored) {
                    LOGGER.info("Update process aborted.");
                    return;
                }

                try (PreparedStatement statement = con.prepareStatement(RENAME_TABLE_QUERY)) {
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = con.prepareStatement(RENAME_UUID_QUERY)) {
                    statement.executeUpdate();
                }

                try (PreparedStatement statement = con.prepareStatement(INSERT_VERSION_QUERY)) {
                    statement.setInt(1, CURRENT_DB_VERSION);
                    statement.setInt(2, CURRENT_DB_VERSION);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String readUser(UUID userUUID) throws UnknownUserException, IOException {
        try (Connection con = source.getConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_USER_QUERY)) {
            statement.setString(1, userUUID.toString());
            ResultSet set = statement.executeQuery();

            if (!set.next()) {
                throw new UnknownUserException(userUUID);
            }

            return new String(set.getBytes("data"));
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public boolean userExists(UUID userUUID) throws IOException {
        try (Connection con = source.getConnection();
             PreparedStatement statement = con.prepareStatement(SELECT_USER_QUERY)) {
            statement.setString(1, userUUID.toString());
            ResultSet set = statement.executeQuery();

            return set.next();
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public List<UUID> listUsers() throws IOException {
        List<UUID> userList = new ArrayList<>();

        try (Connection con = source.getConnection();
             PreparedStatement statement = con.prepareStatement(LIST_USERS_QUERY)) {
            ResultSet set = statement.executeQuery();

            while (set.next()) {
                userList.add(UUID.fromString(set.getString("uuid")));
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }

        return userList;
    }

    @Override
    public void saveUser(UUID userUUID, String userData) throws IOException {
        try (Connection con = source.getConnection();
             PreparedStatement statement = con.prepareStatement(UPDATE_USER_QUERY)) {
            statement.setString(1, userUUID.toString());
            statement.setString(2, userData);
            statement.setString(3, userData);
            statement.executeUpdate();

        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public void deleteUser(UUID userUUID) throws UnknownUserException, IOException {
        try (Connection con = source.getConnection();
             PreparedStatement statement = con.prepareStatement(DELETE_USER_QUERY)) {
            statement.setString(1, userUUID.toString());

            if (statement.executeUpdate() == 0) {
                throw new UnknownUserException(userUUID);
            }
        } catch (SQLException ex) {
            throw new IOException(ex);
        }
    }

    private void init() throws SQLException {
        try (Connection con = source.getConnection()) {
            try (PreparedStatement statement = con.prepareStatement(CREATE_USERS_TABLE_QUERY)) {
                statement.execute();
            }

            try (PreparedStatement statement = con.prepareStatement(CREATE_VERSIONING_TABLE_QUERY)) {
                statement.execute();
            }
        }
    }
}
