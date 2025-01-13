package me.webhead1104.township.commands;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.SubCommand;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

@SubCommand(value = "unlock")
@Description(value = "Unlocks the factory type or animal type")
@SuppressWarnings("unused")
public class UnlockCommand {

    @SubCommand(value = "animal")
    public void unlockAnimal(BukkitSource source, AnimalType type) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getAnimals().setUnlocked(type, true);
        player.sendMessage(Msg.format("<green>Unlocked " + type.name() + "!"));
    }

    @SubCommand(value = "factory")
    public void unlockFactory(BukkitSource source, FactoryType type) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getFactories().setUnlocked(type, true);
        player.sendMessage(Msg.format("<green>Unlocked " + type.name() + "!"));
    }

    @SubCommand(value = "trains")
    public void unlockTrains(BukkitSource source) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getTrains().setUnlocked(true);
        player.sendMessage(Msg.format("<green>Unlocked the trains!"));
    }

    @SubCommand(value = "train")
    public void unlockTrain(BukkitSource source, int train) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getTrains().getTrain(train).setUnlocked(true);
        player.sendMessage(Msg.format("<green>Unlocked the train " + train + "!"));
    }
}
