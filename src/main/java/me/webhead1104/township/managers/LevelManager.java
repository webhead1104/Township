package me.webhead1104.township.managers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
public class LevelManager {

    private final List<Level> levelList = new ArrayList<>();

    public void loadLevels() {
        try {
            long start = System.currentTimeMillis();
            String levels = new String(Objects.requireNonNull(this.getClass().getResourceAsStream("/levels.json")).readAllBytes());
            JsonArray array = Township.getGSON().fromJson(levels, JsonObject.class).get("levels").getAsJsonArray();
            for (JsonElement jsonElement : array) {
                long xp = jsonElement.getAsJsonObject().get("xp_needed").getAsLong();
                long coins = jsonElement.getAsJsonObject().get("coins_given").getAsLong();
                long cash = jsonElement.getAsJsonObject().get("cash_given").getAsLong();
                levelList.add(new Level(xp, coins, cash));
            }
            Township.logger.info(STR."Levels loaded in \{System.currentTimeMillis() - start} mills!");
            for (Level level : levelList) {
                Township.logger.info(STR."xp = \{level.getXpNeeded()} coins = \{level.getCoinsGiven()} cash = \{level.getCashGiven()}");
            }
        } catch (Exception e) {
            Township.logger.error("error", e);
        }
    }
}
