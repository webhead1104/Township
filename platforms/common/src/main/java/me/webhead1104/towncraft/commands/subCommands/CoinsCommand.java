package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import studio.mevera.imperat.annotations.Range;
import studio.mevera.imperat.annotations.SubCommand;
import studio.mevera.imperat.annotations.Usage;

@SubCommand("coins")
public final class CoinsCommand {
    @Usage
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d coins!", user.getCoins()));
    }

    @SubCommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCoins() + amount, player)) {
            return;
        }
        user.setCoins(amount);
        player.sendMessage(Msg.format("<green>Set coins to %d!", amount));
    }

    @SubCommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCoins() + amount, player)) {
            return;
        }
        user.setCoins(user.getCoins() + amount);
        player.sendMessage(Msg.format("<green>Added %d coins!", amount));
    }

    @SubCommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCoins() - amount, player)) {
            return;
        }
        user.setCoins(user.getCoins() - amount);
        player.sendMessage(Msg.format("<green>Removed %d coins!", amount));
    }
}

