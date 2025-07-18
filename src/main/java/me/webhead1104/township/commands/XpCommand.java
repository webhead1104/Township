package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.PlayerLevel;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

public final class XpCommand {

    public static void addXp(Player player, long xp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel(), user.getLevel().getXp() + xp, player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Added %d xp!", xp));
    }

    public static void removeXp(Player player, long xp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel(), user.getLevel().getXp() - xp, player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Removed %d xp!", xp));
    }

    public static void setXp(Player player, long xp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel(), xp, player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Set xp to %d xp!", xp));
    }

    public static void getXp(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        long xp = user.getLevel().getXp();
        player.sendMessage(Msg.format("<green>You have %d xp!", xp));
    }
}
