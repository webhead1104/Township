package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.entity.Player;

public final class XpCommand {

    public static void addXp(Player player, int xp) {
        Towncraft.getUserManager().getUser(player.getUniqueId()).addXp(xp);
        player.sendMessage(Msg.format("<green>Added %d xp!", xp));
    }

    public static void removeXp(Player player, int xp) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setXp(user.getXp() - xp);
        player.sendMessage(Msg.format("<green>Removed %d xp!", xp));
    }

    public static void setXp(Player player, int xp) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setXp(xp);
        player.sendMessage(Msg.format("<green>Set xp to %d xp!", xp));
    }

    public static void getXp(Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d xp!", user.getXp()));
    }
}
