package me.webhead1104.township.commands;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.*;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.PlayerLevel;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

@SubCommand(value = "level")
@Description(value = "The level command")
@Permission(value = "township.commands.level")
@SuppressWarnings("unused")
public class LevelCommand {

    @SubCommand(value = "add")
    public void addLevels(BukkitSource source, @Named(value = "The amount of levels to add") int level) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel() + level, user.getLevel().getXp(), player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Added " + level + " levels!"));
    }

    @SubCommand(value = "remove")
    public void removeLevels(BukkitSource source, @Named(value = "The amount of levels to remove") int level) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel() - level, user.getLevel().getXp(), player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Removed " + level + " levels!"));
    }

    @SubCommand(value = "set")
    public void setLevel(BukkitSource source, int level) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(level, user.getLevel().getXp(), player.getUniqueId()));
    }

    @SubCommand(value = "get")
    public void getLevel(BukkitSource source) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int level = user.getLevel().getLevel();
        player.sendMessage(Msg.format("<green>Your level is " + level + "!"));
    }
}

