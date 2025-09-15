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
            var list = node.getList(Level.class);
            if (list == null || list.isEmpty()) {
                Township.logger.error("No levels found in levels.json!");
                return;
            }
            for (Level level : list) {
                levelMap.put(i++, level);
            }
            Township.logger.info("Loaded {} levels in {} ms!", levelMap.size(), System.currentTimeMillis() - start);
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst loading the levels! Please report the following stacktrace to Webhead1104:", e);
        }
    }

    public void addXp(Player player, int amountOfXp) {
        if (amountOfXp <= 0) return; // ignore non-positive xp changes here
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setXp(user.getXp() + amountOfXp);
        //noinspection StatementWithEmptyBody
        while (canLevelUp(player)) {
        }
    }

    public boolean canLevelUp(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Level nextLevel = Township.getLevelManager().getLevelMap().get(user.getLevel() + 1);
        if (nextLevel == null) {
            return false;
        }
        int needed = nextLevel.getXpNeeded();
        if (user.getXp() >= needed) {
            // level up
            user.setLevel(user.getLevel() + 1);
            // subtract the XP required so overflow carries to next level
            user.setXp(user.getXp() - needed);

            // Apply rewards for reaching this level
            int coinsReward = nextLevel.getCoinsGiven();
            int cashReward = nextLevel.getCashGiven();
            if (coinsReward > 0) user.setCoins(user.getCoins() + coinsReward);
            if (cashReward > 0) user.setCash(user.getCash() + cashReward);

            // Notify player
            StringBuilder sb = new StringBuilder();
            sb.append("<green>You have leveled up to level ").append(user.getLevel()).append("!");
            if (coinsReward > 0 || cashReward > 0) {
                sb.append(" <gray>(Rewards: ");
                boolean added = false;
                if (coinsReward > 0) {
                    sb.append("<yellow>").append(coinsReward).append(" coins");
                    added = true;
                }
                if (cashReward > 0) {
                    sb.append(added ? "<gray>, " : "").append("<aqua>").append(cashReward).append(" cash");
                }
                sb.append("<gray>)");
            }
            player.sendMessage(Msg.format(sb.toString()));
            return true;
        }
        return false;
    }

    public String getProgressBar(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Level nextLevel = Township.getLevelManager().getLevelMap().get(user.getLevel() + 1);
        if (nextLevel != null) {
            int max = nextLevel.getXpNeeded();
            float percent = max > 0 ? Math.min(1f, Math.max(0f, (float) user.getXp() / max)) : 0f;
            int progressBars = Math.max(0, Math.min(16, (int) (16 * percent)));

            return Strings.repeat("<aqua>■", progressBars) + Strings.repeat("<gray>■", 16 - progressBars);
        } else {
            return "<dark_red>You have reached the max level!";
        }
    }
}
