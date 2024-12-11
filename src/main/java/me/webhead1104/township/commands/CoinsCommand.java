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

@SubCommand(value = "coins")
@Description(value = "The coins command")
@Permission(value = "township.commands.coins")
@SuppressWarnings("unused")
public class CoinsCommand {

    @SubCommand(value = "add")
    public void addCoins(BukkitSource source, @Named(value = "The amount of coins to add") long coins) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCoins(user.getCoins() + coins);
        player.sendMessage(Msg.format("<green>Added " + coins + " coins!"));
    }

    @SubCommand(value = "remove")
    public void removeCoins(BukkitSource source, @Named(value = "The amount of coins to remove") long coins) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        if (user.getCoins() - coins < 0) {
            player.sendMessage(Msg.format("<red>Unable to remove " + coins + " coins!"));
            return;
        }
        user.setCoins(user.getCoins() - coins);
        player.sendMessage(Msg.format("<green>Removed " + coins + " coins!"));
    }

    @SubCommand(value = "set")
    public void setCoins(BukkitSource source, long coins) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setCoins(coins);
        player.sendMessage(Msg.format("<green>Set coins to " + coins + "!"));
    }

    @SubCommand(value = "get")
    public void getCoins(BukkitSource source) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        long coins = user.getCoins();
        player.sendMessage(Msg.format("<green>You have " + coins + " coins!"));
    }
}

