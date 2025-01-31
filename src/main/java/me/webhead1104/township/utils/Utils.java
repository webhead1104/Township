package me.webhead1104.township.utils;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Integer> thing(String e, int startAt) {
        List<Integer> list = new ArrayList<>();
        //1x3 is hightXwidth
        switch (e.toLowerCase()) {
            case "2x2" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 9);
                list.add(startAt + 10);
            }
            case "3x3" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 2);
                list.add(startAt + 9);
                list.add(startAt + 10);
                list.add(startAt + 11);
                list.add(startAt + 18);
                list.add(startAt + 19);
                list.add(startAt + 20);
            }
            case "4x4" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 2);
                list.add(startAt + 3);
                list.add(startAt + 9);
                list.add(startAt + 10);
                list.add(startAt + 11);
                list.add(startAt + 12);
                list.add(startAt + 18);
                list.add(startAt + 19);
                list.add(startAt + 20);
                list.add(startAt + 21);
            }
            case "1x3" -> {
                list.add(startAt);
                list.add(startAt + 1);
                list.add(startAt + 2);
            }
        }
        return list;
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
