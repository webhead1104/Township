package me.webhead1104.township.commands;

import me.webhead1104.township.Township;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class MaxPopulationCommand {

    public static void getPopulation(Player player) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        player.sendMessage(Msg.format("<green>You have %d max population!", user.getMaxPopulation()));
    }

    public static void addPopulation(Player player, int maxPopulation) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setMaxPopulation(user.getMaxPopulation() + maxPopulation);
        player.sendMessage(Msg.format("<green>Added %d max population!", maxPopulation));
    }

    public static void removePopulation(Player player, int maxPopulation) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setMaxPopulation(user.getMaxPopulation() - maxPopulation);
        player.sendMessage(Msg.format("<green>Removed %d max population!", maxPopulation));
    }

    public static void setPopulation(Player player, int maxPopulation) {
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setMaxPopulation(maxPopulation);
        player.sendMessage(Msg.format("<green>Set your max population to %d!", maxPopulation));
    }
}
