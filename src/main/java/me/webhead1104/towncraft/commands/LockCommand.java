package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.entity.Player;

public final class LockCommand {

    public static void lockTrains(Player player, boolean locked) {
        Towncraft.getUserManager().getUser(player.getUniqueId()).getTrains().setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set the trains to %b", locked));
    }

    public static void lockTrain(Player player, int train, boolean locked) {
        Towncraft.getUserManager().getUser(player.getUniqueId()).getTrains().getTrain(train).setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set train %d to %b", train, locked));
    }
}
