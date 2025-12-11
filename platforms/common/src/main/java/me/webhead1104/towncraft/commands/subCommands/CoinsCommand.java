package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;

@Subcommand("coins")
public final class CoinsCommand implements TowncraftSubCommand {
    @Subcommand("get")
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d coins!", user.getCoins()));
    }

    @Subcommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCoins() + amount, player)) {
            return;
        }
        user.setCoins(amount);
        player.sendMessage(Msg.format("<green>Set coins to %d!", amount));
    }

    @Subcommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCoins() + amount, player)) {
            return;
        }
        user.setCoins(user.getCoins() + amount);
        player.sendMessage(Msg.format("<green>Added %d coins!", amount));
    }

    @Subcommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCoins() - amount, player)) {
            return;
        }
        user.setCoins(user.getCoins() - amount);
        player.sendMessage(Msg.format("<green>Removed %d coins!", amount));
    }
}

