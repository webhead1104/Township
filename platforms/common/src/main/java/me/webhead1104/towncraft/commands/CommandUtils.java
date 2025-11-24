package me.webhead1104.towncraft.commands;

import lombok.experimental.UtilityClass;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.utils.Msg;

@UtilityClass
public class CommandUtils {
    public static boolean validate(int value, TowncraftPlayer player) {
        return validate(0, Integer.MAX_VALUE, value, player);
    }

    public static boolean validate(int max, int value, TowncraftPlayer player) {
        return validate(0, max, value, player);
    }

    public static boolean validate(int min, int max, int value, TowncraftPlayer player) {
        if (value < min || value > max) {
            player.sendMessage(Msg.format("<red>Invalid number: %d! Please enter a number between %d and %d!", value, min, max));
            return true;
        }
        return false;
    }
}
