package me.webhead1104.township.commands;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SubCommand;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.PlayerLevel;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

@SubCommand(value = "xp")
@Description(value = "The xp command")
@Permission(value = "township.commands.xp")
@SuppressWarnings("unused")
public class XpCommand {

    @SubCommand(value = "add")
    public void addXp(BukkitSource source, @Named(value = "The amount of xp to add") long xp) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel(), user.getLevel().getXp() + xp, player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Added " + xp + " xp!"));
    }

    @SubCommand(value = "remove")
    public void removeXp(BukkitSource source, @Named(value = "The amount of xp to remove") long xp) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel(), user.getLevel().getXp() - xp, player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Removed " + xp + " xp!"));
    }

    @SubCommand(value = "set")
    public void setXp(BukkitSource source, long xp) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setLevel(new PlayerLevel(user.getLevel().getLevel(), xp, player.getUniqueId()));
        player.sendMessage(Msg.format("<green>Set xp to " + xp + " xp!"));
    }

    @SubCommand(value = "get", attachDirectly = true)
    public void getXp(BukkitSource source) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        long xp = user.getLevel().getXp();
        player.sendMessage(Msg.format("<green>You have " + xp + " xp!"));
    }
}
