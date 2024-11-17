package me.webhead1104.township.data.objects;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@Getter
@Setter
@AllArgsConstructor
public class PlayerLevel {
    private int level;
    private int xp;
    private UUID uuid;

    public void addXp(int amountOfXp) {
        User user = Township.getUserManager().getUser(uuid);
        if (canLevelUp()) {
            user.getLevel().setXp(amountOfXp);
        } else {
            user.getLevel().setXp(user.getLevel().getXp() + amountOfXp);
        }
    }

    public boolean canLevelUp() {
        if (!((level + 1) == Township.getLevelManager().getLevelList().size())) {
            User user = Township.getUserManager().getUser(uuid);
            if (user.getLevel().getXp() >= Township.getLevelManager().getLevelList().get(user.getLevel().getLevel() + 1).getXpNeeded()) {
                user.getLevel().setLevel(user.getLevel().getLevel() + 1);
                user.getLevel().setXp(0);
                Objects.requireNonNull(Bukkit.getPlayer(uuid))
                        .sendMessage(MM."<green>You have leveled up! You are now level \{user.getLevel().getLevel()}");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getProgressBar() {
        if (!((level + 1) == Township.getLevelManager().getLevelList().size())) {
            long max = Township.getLevelManager().getLevelList().get(level + 1).getXpNeeded();
            float percent = (float) xp / max;
            int progressBars = (int) (16 * percent);

            return Strings.repeat("<aqua>■", progressBars) + Strings.repeat("<gray>■", 16 - progressBars);
        } else {
            return "<dark_red>You have reached the max level!";
        }
    }
}
