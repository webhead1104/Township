package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import studio.mevera.imperat.annotations.Range;
import studio.mevera.imperat.annotations.SubCommand;
import studio.mevera.imperat.annotations.Usage;

@SubCommand("population")
public final class PopulationCommand {

    @Usage
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d population!", user.getPopulation()));
    }

    @SubCommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getPopulation() + value, player)) {
            return;
        }
        user.setPopulation(value);
        player.sendMessage(Msg.format("<green>Set your population to %d!", value));
    }

    @SubCommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getPopulation() + value, player)) {
            return;
        }
        user.setPopulation(user.getPopulation() + value);
        player.sendMessage(Msg.format("<green>Added %d population!", value));
    }

    @SubCommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getPopulation() + value, player)) {
            return;
        }
        user.setPopulation(user.getPopulation() - value);
        player.sendMessage(Msg.format("<green>Removed %d population!", value));
    }
}
