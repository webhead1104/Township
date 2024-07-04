package me.webhead1104.township.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.webhead1104.township.Township;
import net.cytonic.cytosis.data.DatabaseTemplate;
import net.cytonic.cytosis.logging.Logger;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Database {

    public Database() {
    }

    public static void createTable() {
        try {
            DatabaseTemplate.UPDATE."CREATE TABLE IF NOT EXISTS Township(PlayerUUID VARCHAR NOT NULL PRIMARY KEY, data TEXT NOT NULL;"
                    .whenComplete((_, _) -> {
                    });
        } catch (Exception e) {
            Logger.error("error", e);
        }
    }

    public static JsonObject getData(UUID uuid) {
        AtomicReference<JsonObject> output = new AtomicReference<>();
        DatabaseTemplate.QUERY."SELECT * FROM Township WHERE PlayerUUID = \{uuid.toString()};".whenComplete((rs, throwable) -> {
            if (throwable != null) {
                Logger.error("error", throwable);
            } else {
                try {
                    if (rs.next()) {
                        output.set(new Gson().fromJson(rs.getString("data"), JsonObject.class));
                    }
                } catch (SQLException e) {
                    Logger.error("error", e);
                }
            }
        });
        return output.get();
    }

    public static void setData(UUID uuid, String value) {
        DatabaseTemplate.UPDATE."UPDATE Township SET data = \{value} WHERE PlayerUUID = \{uuid.toString()};".whenComplete((_, _) -> {});
    }

    public static void newPlayer(UUID uuid) {
        JsonObject obj = Township.dataTable;
        obj.addProperty("PlayerUUID", uuid.toString());
        DatabaseTemplate.UPDATE."INSERT INTO Township (PlayerUUID, data)VALUES (\{uuid.toString()},\{obj.toString()});".whenComplete((_, _) -> {});
        Logger.debug("new player!");
    }
}