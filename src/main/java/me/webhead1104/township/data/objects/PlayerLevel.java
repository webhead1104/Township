package me.webhead1104.township.data.objects;

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
        if (canLevelUp(uuid)) {
            user.getLevel().setXp(amountOfXp);
        } else {
            user.getLevel().setXp(user.getLevel().getXp() + amountOfXp);
        }
    }

    public boolean canLevelUp(UUID uuid) {
        User user = Township.getUserManager().getUser(uuid);
        if (user.getLevel().getXp() >= Township.getLevelManager().getLevelList().get(user.getLevel().getLevel()).getXpNeeded()) {
            user.getLevel().setLevel(user.getLevel().getLevel() + 1);
            user.getLevel().setXp(0);
            Objects.requireNonNull(Bukkit.getPlayer(uuid))
                    .sendMessage(MM."<green>You have leveled up! You are now level \{user.getLevel().getLevel()}");
            return true;
        } else {
            return false;
        }
    }
}
