package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.dataLoaders.LevelDataLoader;
import me.webhead1104.towncraft.utils.Msg;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Executor;
import net.strokkur.commands.annotations.arguments.IntArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class LevelCommand {

    @Executes
    public void get(CommandSender $, @Executor Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>Your level is %d!", user.getLevel()));
    }

    @Executes("set")
    public void set(CommandSender $, @Executor Player player, @IntArg(min = 0, max = LevelDataLoader.MAX_LEVELS_ADDED) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(LevelDataLoader.MAX_LEVELS_ADDED, user.getLevel() + amount, player)) {
            return;
        }
        user.setLevel(amount);
        player.sendMessage(Msg.format("<green>Set your level to %d!", amount));
    }

    @Executes("add")
    public void add(CommandSender $, @Executor Player player, @IntArg(min = 0, max = LevelDataLoader.MAX_LEVELS_ADDED) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(LevelDataLoader.MAX_LEVELS_ADDED, user.getLevel() + amount, player)) {
            return;
        }
        user.setLevel(user.getLevel() + amount);
        player.sendMessage(Msg.format("<green>Added %d levels!", amount));
    }

    @Executes("remove")
    public void remove(CommandSender $, @Executor Player player, @IntArg(min = 0, max = LevelDataLoader.MAX_LEVELS_ADDED) int amount) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        if (CommandUtils.validate(LevelDataLoader.MAX_LEVELS_ADDED, user.getLevel() - amount, player)) {
            return;
        }
        user.setLevel(user.getLevel() - amount);
        player.sendMessage(Msg.format("<green>Removed %d levels!", amount));
    }
}

