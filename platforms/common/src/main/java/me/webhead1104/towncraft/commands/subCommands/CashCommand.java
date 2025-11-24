package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import studio.mevera.imperat.annotations.Range;
import studio.mevera.imperat.annotations.SubCommand;
import studio.mevera.imperat.annotations.Usage;

@SubCommand("cash")
public final class CashCommand {
    @Usage
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d cash!", user.getCash()));
    }

    @SubCommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(amount);
        player.sendMessage(Msg.format("<green>Set cash to %d!", amount));
    }

    @SubCommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(user.getCash() + amount);
        player.sendMessage(Msg.format("<green>Added %d cash!", amount));
    }

    @SubCommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() - amount, player)) {
            return;
        }
        user.setCash(user.getCash() - amount);
        player.sendMessage(Msg.format("<green>Removed %s cash!", amount));
    }
}

