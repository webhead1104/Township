package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

public class ItemCommand {

    public static void addItem(Player player, int amount, ItemType itemType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getBarn().addAmountToItem(itemType, amount);
        player.sendMessage(Msg.format("<green>Added %d to %s!", amount, itemType.name()));
    }

    public static void removeItem(Player player, int amount, ItemType itemType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getBarn().getItem(itemType) - amount < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove %s amount from %s!", amount, itemType.name()));
            return;
        }
        user.getBarn().removeAmountFromItem(itemType, amount);
        player.sendMessage(Msg.format("<green>Removed %s amount!", amount));
    }

    public static void setItem(Player player, int amount, ItemType itemType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.getBarn().setItem(itemType, amount);
        player.sendMessage(Msg.format("<green>Set %s to %d!", itemType.name(), amount));
    }

    public static void getItem(Player player, ItemType itemType) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d of %s!", user.getBarn().getItem(itemType), itemType.name()));
    }
}
