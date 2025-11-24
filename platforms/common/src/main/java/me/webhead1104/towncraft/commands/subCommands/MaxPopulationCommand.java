package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import studio.mevera.imperat.annotations.Range;
import studio.mevera.imperat.annotations.SubCommand;
import studio.mevera.imperat.annotations.Usage;

@SubCommand("maxpopulation")
public final class MaxPopulationCommand {

    @Usage
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d max population!", user.getMaxPopulation()));
    }

    @SubCommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getMaxPopulation() + value, player)) {
            return;
        }
        user.setMaxPopulation(value);
        player.sendMessage(Msg.format("<green>Set your max population to %d!", value));
    }

    @SubCommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getMaxPopulation() + value, player)) {
            return;
        }
        user.setMaxPopulation(user.getMaxPopulation() + value);
        player.sendMessage(Msg.format("<green>Added %d max population!", value));
    }

    @SubCommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getMaxPopulation() + value, player)) {
            return;
        }
        user.setMaxPopulation(user.getMaxPopulation() - value);
        player.sendMessage(Msg.format("<green>Removed %d max population!", value));
    }
}
