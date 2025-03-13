package me.webhead1104.township.commands;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.SubCommand;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.utils.Msg;
import org.bukkit.entity.Player;

@SubCommand(value = "lock")
@Description(value = "Locks the factory type or animal type")
@SuppressWarnings("unused")
public class LockCommand {

    @SubCommand(value = "animal")
    public void lock(BukkitSource source, AnimalType type) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getAnimals().getAnimalBuilding(type).setUnlocked(false);
        player.sendMessage(Msg.format("<green>Locked" + type.name()));
    }

    @SubCommand(value = "factory")
    public void lock(BukkitSource source, FactoryType type) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getFactories().getFactory(type).setUnlocked(false);
        player.sendMessage(Msg.format("<green>Locked " + type.name()));
    }

    @SubCommand(value = "trains")
    public void lockTrains(BukkitSource source) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getTrains().setUnlocked(false);
        player.sendMessage(Msg.format("<green>Locked the trains!"));
    }

    @SubCommand(value = "train")
    public void lockTrain(BukkitSource source, int train) {
        Player player = source.asPlayer();
        Township.getUserManager().getUser(player.getUniqueId()).getTrains().getTrain(train).setUnlocked(false);
        player.sendMessage(Msg.format("<green>Locked train " + train));
    }
}
