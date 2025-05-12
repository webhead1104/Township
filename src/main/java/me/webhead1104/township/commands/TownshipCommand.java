package me.webhead1104.township.commands;

import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.*;
import dev.velix.imperat.help.CommandHelp;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import org.bukkit.entity.Player;

@Command("township")
@Description("The main command for Township")
@SuppressWarnings("unused")
@Inherit({
        PopulationCommand.class,
        MaxPopulationCommand.class,
        LevelCommand.class,
        XpCommand.class,
        CoinsCommand.class,
        CashCommand.class,
        UnlockCommand.class,
        LockCommand.class})
public final class TownshipCommand {

    @Usage
    public void mainCommand(BukkitSource source) {
        Player player = source.asPlayer();
        if (!player.getInventory().isEmpty()) Township.getInventoryManager().addPlayerInventory(player);
        player.getInventory().clear();
        Township.getWorldManager().openWorldMenu(player);
    }

    @SubCommand(value = "help")
    public void test(BukkitSource source, CommandHelp commandHelp) {
        source.asPlayer().sendMessage("Hello World This is the help message!");
    }

    @SubCommand(value = "animals")
    public void animals(BukkitSource source, AnimalType animalType) {
        Township.getAnimalsManager().openAnimalMenu(source.asPlayer(), animalType);
    }

    @SubCommand(value = "factories")
    public void factories(BukkitSource source, FactoryType factoryType) {
        Township.getFactoriesManager().openFactoryMenu(source.asPlayer(), factoryType);
    }
}
