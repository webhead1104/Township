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

public final class CashCommand {
    @Executes
    public void get(CommandSender $, @Executor Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d cash!", user.getCash()));
    }

    @Executes("set")
    public void set(CommandSender $, @Executor Player player, @IntArg(min = 0) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(amount);
        player.sendMessage(Msg.format("<green>Set cash to %d!", amount));
    }

    @Executes("add")
    public void add(CommandSender $, @Executor Player player, @IntArg(min = 0) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(user.getCash() + amount);
        player.sendMessage(Msg.format("<green>Added %d cash!", amount));
    }

    @Executes("remove")
    public void remove(CommandSender $, @Executor Player player, @IntArg(min = 0) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(user.getCash() - amount, player)) {
            return;
        }
        user.setCash(user.getCash() - amount);
        player.sendMessage(Msg.format("<green>Removed %s cash!", amount));
    }
}

