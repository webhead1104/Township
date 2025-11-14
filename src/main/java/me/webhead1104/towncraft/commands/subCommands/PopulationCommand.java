package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Executor;
import net.strokkur.commands.annotations.arguments.IntArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class PopulationCommand {

    @Executes
    public void get(CommandSender $, @Executor Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d population!", user.getPopulation()));
    }

    @Executes("set")
    public void set(CommandSender $, @Executor Player player, @IntArg(min = 0) int value) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getPopulation() + value, player)) {
            return;
        }
        user.setPopulation(value);
        player.sendMessage(Msg.format("<green>Set your population to %d!", value));
    }

    @Executes("add")
    public void add(CommandSender $, @Executor Player player, @IntArg(min = 0) int value) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getPopulation() + value, player)) {
            return;
        }
        user.setPopulation(user.getPopulation() + value);
        player.sendMessage(Msg.format("<green>Added %d population!", value));
    }

    @Executes("remove")
    public void remove(CommandSender $, @Executor Player player, @IntArg(min = 0) int value) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getPopulation() + value, player)) {
            return;
        }
        user.setPopulation(user.getPopulation() - value);
        player.sendMessage(Msg.format("<green>Removed %d population!", value));
    }
}
