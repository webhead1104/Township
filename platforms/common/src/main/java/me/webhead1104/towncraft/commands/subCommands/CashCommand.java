/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
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
package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.commands.CommandUtils;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.utils.Msg;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;

@Subcommand("cash")
public final class CashCommand implements TowncraftSubCommand {
    @Subcommand("get")
    public void get(TowncraftPlayer player) {
        User user = player.getUser();
        player.sendMessage(Msg.format("<green>You have %d cash!", user.getCash()));
    }

    @Subcommand("set")
    public void set(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(amount);
        player.sendMessage(Msg.format("<green>Set cash to %d!", amount));
    }

    @Subcommand("add")
    public void add(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() + amount, player)) {
            return;
        }
        user.setCash(user.getCash() + amount);
        player.sendMessage(Msg.format("<green>Added %d cash!", amount));
    }


    @Subcommand("remove")
    public void remove(TowncraftPlayer player, @Range(min = 0) int amount) {
        User user = player.getUser();
        if (CommandUtils.validate(user.getCash() - amount, player)) {
            return;
        }
        user.setCash(user.getCash() - amount);
        player.sendMessage(Msg.format("<green>Removed %s cash!", amount));
    }
}

