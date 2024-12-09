package me.webhead1104.township.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class LevelManager {

    private final Map<Integer, Level> levelMap = new HashMap<>();

    public void loadLevels() {
        try {
            long start = System.currentTimeMillis();
            String levels = new String(Objects.requireNonNull(this.getClass().getResourceAsStream("/levels.json")).readAllBytes());
            JsonArray array = Township.getGSON().fromJson(levels, JsonObject.class).get("levels").getAsJsonArray();
            int i = 1;
            for (JsonElement jsonElement : array) {
                long xp = jsonElement.getAsJsonObject().get("xp_needed").getAsLong();
                long coins = jsonElement.getAsJsonObject().get("coins_given").getAsLong();
                long cash = jsonElement.getAsJsonObject().get("cash_given").getAsLong();
                levelMap.put(i, new Level(xp, coins, cash));
                i++;
            }
            Township.logger.info("Levels loaded in {} mills!", System.currentTimeMillis() - start);
        } catch (Exception e) {
            Township.logger.error("error", e);
        }
    }
}
