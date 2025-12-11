package me.webhead1104.towncraft.commands.subCommands;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;

@Keep
@Subcommand("maxpopulation")
public final class MaxPopulationCommand implements TowncraftSubCommand {
    @Subcommand("get")
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d max population!", user.getMaxPopulation()));
    }

    @Subcommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getMaxPopulation() + value, player)) {
            return;
        }
        user.setMaxPopulation(value);
        player.sendMessage(Msg.format("<green>Set your max population to %d!", value));
    }

    @Subcommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getMaxPopulation() + value, player)) {
            return;
        }
        user.setMaxPopulation(user.getMaxPopulation() + value);
        player.sendMessage(Msg.format("<green>Added %d max population!", value));
    }

    @Subcommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int value) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getMaxPopulation() + value, player)) {
            return;
        }
        user.setMaxPopulation(user.getMaxPopulation() - value);
        player.sendMessage(Msg.format("<green>Removed %d max population!", value));
    }
}
