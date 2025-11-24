package me.webhead1104.towncraft.utils;

import lombok.experimental.UtilityClass;
import me.webhead1104.towncraft.items.TowncraftItemStack;
import me.webhead1104.towncraft.items.TowncraftMaterial;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.Instant;

@UtilityClass
public class Utils {

    public static String thing2(String string) {
        return StringUtils.capitalize(string.replaceAll("_", " ").toLowerCase());
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


    public static TowncraftItemStack getItemStack(String name, TowncraftMaterial material) {
        TowncraftItemStack itemStack = TowncraftItemStack.of(material);
        itemStack.setName(Msg.format("<white>%s", name));
        return itemStack;
    }

    public Component addResourceLine(String resourceName, int current, int required, Object... args) {
        resourceName = resourceName.formatted(args);
        String color = current >= required ? "<green>" : "<red>";
        return Msg.format("%s<white>: %s%d/%d", resourceName, color, current, required);
    }
}
