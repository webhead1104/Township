package me.webhead1104.township.data.objects;

import com.google.common.base.Strings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.datafixer.TownshipCodecs;
import me.webhead1104.township.utils.Msg;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class PlayerLevel {
    public static final @NotNull Codec<PlayerLevel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("level").forGetter(PlayerLevel::getLevel),
            Codec.LONG.fieldOf("xp").forGetter(PlayerLevel::getXp),
            TownshipCodecs.UUID.fieldOf("uuid").forGetter(PlayerLevel::getUuid)
    ).apply(instance, PlayerLevel::new));
    private int level;
    private long xp;
    private UUID uuid;

    public PlayerLevel(int level, long xp, UUID uuid) {
        this.level = level;
        this.xp = xp;
        this.uuid = uuid;
    }

    public void addXp(int amountOfXp) {
        User user = Township.getUserManager().getUser(uuid);
        if (canLevelUp()) {
            user.getLevel().setXp(amountOfXp);
        } else {
            user.getLevel().setXp(user.getLevel().getXp() + amountOfXp);
        }
    }

    public boolean canLevelUp() {
        if (Township.getLevelManager().getLevelMap().containsKey(level + 1)) {
            User user = Township.getUserManager().getUser(uuid);
            if (user.getLevel().getXp() >= Township.getLevelManager().getLevelMap().get(user.getLevel().getLevel() + 1).getXpNeeded()) {
                user.getLevel().setLevel(user.getLevel().getLevel() + 1);
                user.getLevel().setXp(0);
                Objects.requireNonNull(Bukkit.getPlayer(uuid))
                        .sendMessage(Msg.format("<green>You have leveled up! You are now level " + user.getLevel().getLevel()));
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public String getProgressBar() {
        if (Township.getLevelManager().getLevelMap().containsKey(level + 1)) {
            long max = Township.getLevelManager().getLevelMap().get(level + 1).getXpNeeded();
            float percent = (float) xp / max;
            int progressBars = (int) (16 * percent);

            return Strings.repeat("<aqua>■", progressBars) + Strings.repeat("<gray>■", 16 - progressBars);
        } else {
            return "<dark_red>You have reached the max level!";
        }
    }
}
