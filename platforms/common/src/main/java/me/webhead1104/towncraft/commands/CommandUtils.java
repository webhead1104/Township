/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
