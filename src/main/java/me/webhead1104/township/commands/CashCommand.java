package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

public final class CashCommand {

    public static void addCash(Player player, long cash) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCash(user.getCash() + cash);
        player.sendMessage(Msg.format("<green>Added %d cash!", cash));
    }

    public static void removeCash(Player player, long cash) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getCash() - cash < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove %s cash!", cash));
            return;
        }
        user.setCash(user.getCash() - cash);
        player.sendMessage(Msg.format("<green>Removed %s cash!", cash));
    }

    public static void setCash(Player player, long cash) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCash(cash);
        player.sendMessage(Msg.format("<green>Set cash to %d!", cash));
    }

    public static void getCash(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d cash!", user.getCash()));
    }
}

