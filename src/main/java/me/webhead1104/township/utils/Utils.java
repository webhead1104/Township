package me.webhead1104.township.utils;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;

@NoArgsConstructor
public class Utils {

    public static void openInventory(Player player, Inventory inventory, Consumer<UUID> handler, BukkitTask bukkitTask) {
        Township.getUserManager().removePlayerCloseHandler(player.getUniqueId());
        player.openInventory(inventory);
        Township.getUserManager().menuTasks.put(player.getUniqueId(), bukkitTask);
        Township.getUserManager().addPlayerCloseHandler(player.getUniqueId(), handler);
    }

    public static String thing2(String string) {
        return StringUtils.capitalize(string.replaceAll("_", " "));
    }

    public static String format(Instant instant1, Instant instant2) {
        Duration duration = Duration.between(instant1, instant2);
        long days = duration.toDays();
        duration = duration.minusDays(days);

        long hours = duration.toHours();
        duration = duration.minusHours(hours);

        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        long seconds = duration.getSeconds();

        StringBuilder formatted = new StringBuilder();

        if (days > 0) {
            formatted.append(days).append("d ");
        }
        if (hours > 0) {
            formatted.append(hours).append("h ");
        }
        if (minutes > 0) {
            formatted.append(minutes).append("m ");
        }
        if (seconds > 0) {
            formatted.append(seconds).append("s");
        }

        return formatted.toString().trim(); // Remove trailing space
    }
}
