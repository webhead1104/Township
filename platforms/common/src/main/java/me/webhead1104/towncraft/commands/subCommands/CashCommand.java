package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;

@Subcommand("cash")
public final class CashCommand implements TowncraftSubCommand {
    @Subcommand("get")
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d cash!", user.getCash()));
    }

    @Subcommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(amount);
        player.sendMessage(Msg.format("<green>Set cash to %d!", amount));
    }

    @Subcommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(user.getCash() + amount);
        player.sendMessage(Msg.format("<green>Added %d cash!", amount));
    }


    @Subcommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() - amount, player)) {
            return;
        }
        user.setCash(user.getCash() - amount);
        player.sendMessage(Msg.format("<green>Removed %s cash!", amount));
    }
}

