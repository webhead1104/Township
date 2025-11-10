package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.entity.Player;

public final class LevelCommand {

    public static void addLevels(Player player, int level) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setLevel(user.getLevel() + level);
        player.sendMessage(Msg.format("<green>Added %d levels!", level));
    }

    public static void removeLevels(Player player, int level) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setLevel(user.getLevel() - level);
        player.sendMessage(Msg.format("<green>Removed %d levels!", level));
    }

    public static void setLevel(Player player, int level) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setLevel(level);
        player.sendMessage(Msg.format("<green>Set your level to %d!", level));
    }

    public static void getLevel(Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>Your level is %d!", user.getLevel()));
    }
}

