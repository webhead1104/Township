package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.utils.Msg;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Executor;
import net.strokkur.commands.annotations.arguments.IntArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class LockCommand {

    @Executes
    public void lockTrain(CommandSender $, @Executor Player player, boolean locked) {
        Towncraft.getUserManager().getUser(player.getUniqueId()).getTrains().setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set the trains to %b", locked));
    }

    @Executes
    public void lockTrain(CommandSender $, @Executor Player player, boolean locked, @IntArg(min = 1, max = 3) int train) {
        Towncraft.getUserManager().getUser(player.getUniqueId()).getTrains().getTrain(train).setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set train %d to %b", train, locked));
    }
}
