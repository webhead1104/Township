package me.webhead1104.township.managers;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.Level;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;
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

    public void addXp(Player player, int amountOfXp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (canLevelUp(player)) {
            user.setXp(amountOfXp);
        } else {
            user.setXp(user.getXp() + amountOfXp);
        }
    }

    public boolean canLevelUp(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (Township.getLevelManager().getLevelMap().containsKey(user.getLevel() + 1)) {
            if (user.getXp() >= Township.getLevelManager().getLevelMap().get(user.getLevel() + 1).getXpNeeded()) {
                user.setLevel(user.getLevel() + 1);
                user.setXp(0);
                player.sendMessage(Msg.format("<green>You have leveled up! You are now level " + user.getLevel()));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getProgressBar(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (Township.getLevelManager().getLevelMap().containsKey(user.getLevel() + 1)) {
            long max = Township.getLevelManager().getLevelMap().get(user.getLevel() + 1).getXpNeeded();
            float percent = (float) user.getXp() / max;
            int progressBars = (int) (16 * percent);

            return Strings.repeat("<aqua>■", progressBars) + Strings.repeat("<gray>■", 16 - progressBars);
        } else {
            return "<dark_red>You have reached the max level!";
        }
    }
}
