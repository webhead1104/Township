package me.webhead1104.towncraft.commands.subCommands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.utils.Msg;
import studio.mevera.imperat.annotations.Range;
import studio.mevera.imperat.annotations.SubCommand;
import studio.mevera.imperat.annotations.Usage;

@SubCommand("lock")
public final class LockCommand {

    @Usage
    public void lockTrain(TowncraftPlayer player, boolean locked) {
        player.getUser().getTrains().setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set the trains to %b", locked));
    }

    @Usage
    public void lockTrain(TowncraftPlayer player, boolean locked, @Range(min = 1, max = 3) int train) {
        player.getUser().getTrains().getTrain(train).setUnlocked(locked);
        player.sendMessage(Msg.format("<green>Set train %d to %b", train, locked));
    }
}
