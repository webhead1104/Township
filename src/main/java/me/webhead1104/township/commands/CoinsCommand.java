package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

public final class CoinsCommand {

    public static void addCoins(Player player, long coins) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCoins(user.getCoins() + coins);
        player.sendMessage(Msg.format("<green>Added %d coins!", coins));
    }

    public static void removeCoins(Player player, long coins) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getCoins() - coins < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove %d coins!", coins));
            return;
        }
        user.setCoins(user.getCoins() - coins);
        player.sendMessage(Msg.format("<green>Removed %d coins!", coins));
    }

    public static void setCoins(Player player, long coins) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCoins(coins);
        player.sendMessage(Msg.format("<green>Set coins to %d!", coins));
    }

    public static void getCoins(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        long coins = user.getCoins();
        player.sendMessage(Msg.format("<green>You have %d coins!", coins));
    }
}

