package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;

public class ItemCommand {

    public static void addItem(Player player, int amount, Key key) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getBarn().addAmountToItem(key, amount);
        player.sendMessage(Msg.format("<green>Added %d to %s!", amount, key.value()));
    }

    public static void removeItem(Player player, int amount, Key key) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getBarn().getItem(key) - amount < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove %s amount from %s!", amount, key.value()));
            return;
        }
        user.getBarn().removeAmountFromItem(key, amount);
        player.sendMessage(Msg.format("<green>Removed %s amount!", amount));
    }

    public static void setItem(Player player, int amount, Key key) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getBarn().setItem(key, amount);
        player.sendMessage(Msg.format("<green>Set %s to %d!", key.value(), amount));
    }

    public static void getItem(Player player, Key key) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d of %s!", user.getBarn().getItem(key), key.value()));
    }
}
