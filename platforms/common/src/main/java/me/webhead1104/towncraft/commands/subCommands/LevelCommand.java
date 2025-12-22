package me.webhead1104.towncraft.commands.subCommands;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.dataLoaders.LevelDataLoader;
import me.webhead1104.towncraft.utils.Msg;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;

@Keep
@Subcommand("level")
public final class LevelCommand implements TowncraftSubCommand {
    @Subcommand("get")
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>Your level is %d!", user.getLevel()));
    }

    @Subcommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0, max = LevelDataLoader.MAX_LEVELS_ADDED) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(LevelDataLoader.MAX_LEVELS_ADDED, amount, player)) {
            return;
        }
        user.setLevel(amount);
        player.sendMessage(Msg.format("<green>Set your level to %d!", amount));
    }

    @Subcommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0, max = LevelDataLoader.MAX_LEVELS_ADDED) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(LevelDataLoader.MAX_LEVELS_ADDED, user.getLevel() + amount, player)) {
            return;
        }
        user.setLevel(user.getLevel() + amount);
        player.sendMessage(Msg.format("<green>Added %d levels!", amount));
    }

    @Subcommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0, max = LevelDataLoader.MAX_LEVELS_ADDED) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(LevelDataLoader.MAX_LEVELS_ADDED, user.getLevel() - amount, player)) {
            return;
        }
        user.setLevel(user.getLevel() - amount);
        player.sendMessage(Msg.format("<green>Removed %d levels!", amount));
    }
}

