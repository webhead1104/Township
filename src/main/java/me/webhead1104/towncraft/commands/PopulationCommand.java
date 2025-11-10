package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import org.bukkit.entity.Player;

public final class PopulationCommand {

    public static void addPopulation(Player player, int population) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setPopulation(user.getPopulation() + population);
        player.sendMessage(Msg.format("<green>Added %d population!", population));
    }

    public static void removePopulation(Player player, int population) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setPopulation(user.getPopulation() - population);
        player.sendMessage(Msg.format("<green>Removed %d population!", population));
    }

    public static void setPopulation(Player player, int population) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        user.setPopulation(population);
        player.sendMessage(Msg.format("<green>Set your population to %d!", population));
    }

    public static void getPopulation(Player player) {
        User user = Towncraft.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d population!", user.getPopulation()));
    }
}
