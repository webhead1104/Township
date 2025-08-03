package me.webhead1104.township.managers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Level;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
            ConfigurationNode node = Township.GSON_CONFIGURATION_LOADER.source(() -> new BufferedReader(new InputStreamReader(Objects.requireNonNull(LevelManager.class.getResourceAsStream("/levels.json"))))).build().load();
            int i = 1;
            for (Level level : Objects.requireNonNull(node.getList(Level.class))) {
                levelMap.put(i++, level);
            }
            Township.logger.info("Levels loaded in {} mills!", System.currentTimeMillis() - start);
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst loading the levels! Please report the following stacktrace to Webhead1104:", e);
        }
    }
}
