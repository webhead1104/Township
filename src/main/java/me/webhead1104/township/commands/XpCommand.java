package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

public final class XpCommand {

    public static void addXp(Player player, long xp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        Township.getLevelManager().addXp(player, xp);
        player.sendMessage(Msg.format("<green>Added %d xp!", xp));
    }

    public static void removeXp(Player player, long xp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setXp(user.getXp() - xp);
        player.sendMessage(Msg.format("<green>Removed %d xp!", xp));
    }

    public static void setXp(Player player, long xp) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setXp(xp);
        player.sendMessage(Msg.format("<green>Set xp to %d xp!", xp));
    }

    public static void getXp(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d xp!", user.getXp()));
    }
}
