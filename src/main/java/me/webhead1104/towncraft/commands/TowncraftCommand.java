package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.commands.arguments.AnimalTypeArgument;
import me.webhead1104.towncraft.commands.arguments.FactoryTypeArgument;
import me.webhead1104.towncraft.commands.subCommands.*;
import me.webhead1104.towncraft.features.animals.AnimalMenu;
import me.webhead1104.towncraft.features.animals.AnimalType;
import me.webhead1104.towncraft.features.factories.FactoryMenu;
import me.webhead1104.towncraft.features.world.WorldUtils;
import net.strokkur.commands.annotations.*;
import net.strokkur.commands.annotations.arguments.CustomArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("towncraft")
@Description("The main Towncraft command.")
public final class TowncraftCommand {
    @Subcommand("cash")
    CashCommand cashCommand;
    @Subcommand("coins")
    CoinsCommand coinsCommand;
    @Subcommand("item")
    ItemCommand itemCommand;
    @Subcommand("level")
    LevelCommand levelCommand;
    @Subcommand("lock train")
    LockCommand lockCommand;
    @Subcommand("maxPopulation")
    MaxPopulationCommand maxPopulationCommand;
    @Subcommand("population")
    PopulationCommand populationCommand;
    @Subcommand("xp")
    XpCommand xpCommand;

    @Executes
    void execute(CommandSender $, @Executor Player player) {
        Towncraft.getInventoryManager().addPlayerInventory(player);
        player.getInventory().clear();
        WorldUtils.openWorldMenu(player);
    }

    @Executes("animal")
    void animals(CommandSender $, @Executor Player player, @CustomArg(AnimalTypeArgument.class) AnimalType.Animal animalType) {
        Towncraft.getViewFrame().open(AnimalMenu.class, player, animalType.key());
    }

    @Executes("factory")
    void factories(CommandSender $, @Executor Player player, @CustomArg(FactoryTypeArgument.class) AnimalType.Animal factoryType) {
        Towncraft.getViewFrame().open(FactoryMenu.class, player, factoryType.key());
    }
}
