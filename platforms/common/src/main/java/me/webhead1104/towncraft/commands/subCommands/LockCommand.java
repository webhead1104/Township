package me.webhead1104.towncraft.commands.subCommands;

import com.google.errorprone.annotations.Keep;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.utils.Msg;
import revxrsal.commands.annotation.Range;
import revxrsal.commands.annotation.Subcommand;

@Keep
public final class LockCommand implements TowncraftSubCommand {

    @Subcommand("lock")
    public void lockTrains(TowncraftPlayer player, boolean locked) {
        player.getUser().getTrains().setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set the trains to %b", locked));
    }

    @Subcommand("lock")
    public void lockTrain(TowncraftPlayer player, boolean locked, @Range(min = 1, max = 3) int train) {
        player.getUser().getTrains().getTrain(train).setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set train %d to %b", train, locked));
    }
}
