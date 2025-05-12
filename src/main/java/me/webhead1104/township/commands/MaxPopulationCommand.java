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

@SubCommand(value = "maxPopulation")
@Description(value = "The max population command")
@Permission(value = "township.commands.population")
@SuppressWarnings("unused")
public class MaxPopulationCommand {

    @SubCommand(value = "add")
    public void addPopulation(BukkitSource source, @Named(value = "The amount of max population to add") int population) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setPopulation(user.getPopulation() + population);
        player.sendMessage(Msg.format("<green>Added " + population + " max population!"));
    }

    @SubCommand(value = "remove")
    public void removePopulation(BukkitSource source, @Named(value = "The amount of max population to remove") int population) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setPopulation(user.getPopulation() - population);
        player.sendMessage(Msg.format("<green>Removed " + population + " max population!"));
    }

    @SubCommand(value = "set")
    public void setPopulation(BukkitSource source, int population) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setPopulation(population);
        player.sendMessage(Msg.format("<green>Set your max population to " + population + "!"));
    }

    @SubCommand(value = "get")
    public void getPopulation(BukkitSource source) {
        Player player = source.asPlayer();
        User user = Township.getUserManager().getUser(player.getUniqueId());
        int population = user.getPopulation();
        player.sendMessage(Msg.format("<green>You have " + population + " max population!"));
    }
}
