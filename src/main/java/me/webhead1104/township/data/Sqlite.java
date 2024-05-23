package me.webhead1104.township.data;

import me.webhead1104.township.Township;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class Sqlite {

    public final Connection connection;

    public Sqlite() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:"+Township.INSTANCE.getDataFolder().getAbsolutePath() + "/township.db");
        try (PreparedStatement ps = connection.prepareStatement("""
                CREATE TABLE IF NOT EXISTS Township(PlayerUUID VARCHAR NOT NULL UNIQUE, data TEXT NOT NULL);""")) {
            ps.execute();
            Township.INSTANCE.getLogger().log(Level.INFO,"Sqlite loaded!");
        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, "Database did not load! error: " + e);
        }
    }
}
