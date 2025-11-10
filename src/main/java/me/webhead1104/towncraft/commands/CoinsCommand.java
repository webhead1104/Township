package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.entity.Player;

public final class CoinsCommand {

    public static void addCoins(Player player, int coins) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setCoins(user.getCoins() + coins);
        player.sendMessage(Msg.format("<green>Added %d coins!", coins));
    }

    public static void removeCoins(Player player, int coins) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (user.getCoins() - coins < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove %d coins!", coins));
            return;
        }
        user.setCoins(user.getCoins() - coins);
        player.sendMessage(Msg.format("<green>Removed %d coins!", coins));
    }

    public static void setCoins(Player player, int coins) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setCoins(coins);
        player.sendMessage(Msg.format("<green>Set coins to %d!", coins));
    }

    public static void getCoins(Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        int coins = user.getCoins();
        player.sendMessage(Msg.format("<green>You have %d coins!", coins));
    }
}

