package me.webhead1104.township.commands;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SubCommand;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

@SubCommand(value = "cash")
@Description(value = "The cash command")
@Permission(value = "township.commands.cash")
@SuppressWarnings("unused")
public class CashCommand {

    @SubCommand(value = "add")
    public void addCash(BukkitSource source, @Named(value = "The amount of cash to add") long cash) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCash(user.getCash() + cash);
        player.sendMessage(Msg.format("<green>Added " + cash + " cash!"));
    }

    @SubCommand(value = "remove")
    public void removeCash(BukkitSource source, @Named(value = "The amount of cash to remove") long cash) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getCash() - cash < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove " + cash + " cash!"));
            return;
        }
        user.setCash(user.getCash() - cash);
        player.sendMessage(Msg.format("<green>Removed " + cash + " cash!"));
    }

    @SubCommand(value = "set")
    public void setCash(BukkitSource source, long cash) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCash(cash);
        player.sendMessage(Msg.format("<green>Set cash to " + cash + "!"));
    }

    @SubCommand(value = "get")
    public void getCash(BukkitSource source) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        long cash = user.getCash();
        player.sendMessage(Msg.format("<green>You have " + cash + " cash!"));
    }
}

