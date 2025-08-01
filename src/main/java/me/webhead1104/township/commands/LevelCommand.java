package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.PlayerLevel;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

public final class LevelCommand {

    public static void addLevels(Player player, int level) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel() + level, user.getLevel().getXp(), player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Added %d levels!", level));
    }

    public static void removeLevels(Player player, int level) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel() - level, user.getLevel().getXp(), player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Removed %d levels!", level));
    }

    public static void setLevel(Player player, int level) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(level, user.getLevel().getXp(), player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Set your level to %d!", level));
    }

    public static void getLevel(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int level = user.getLevel().getLevel();
        player.sendMessage(Msg.format("<green>Your level is %d!", level));
    }
}

