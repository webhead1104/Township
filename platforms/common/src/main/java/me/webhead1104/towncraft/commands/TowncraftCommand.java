package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlatformManager;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.features.animals.AnimalMenu;
import me.webhead1104.towncraft.features.animals.AnimalType;
import me.webhead1104.towncraft.features.factories.FactoryMenu;
import me.webhead1104.towncraft.features.factories.FactoryType;
import me.webhead1104.towncraft.features.world.WorldUtils;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.annotation.Usage;

@Command("towncraft")
@Description("The main Towncraft command")
public class TowncraftCommand {
    @Usage("towncraft")
    public void execute(TowncraftPlayer player) {
        TowncraftPlatformManager.getInventoryManager().addPlayerInventory(player);
        player.getInventory().clear();
        WorldUtils.openWorldMenu(player);
    }

    @Subcommand("animal")
    public void animals(TowncraftPlayer player, AnimalType.Animal animalType) {
        Towncraft.getViewFrame().open(AnimalMenu.class, player, animalType.key());
    }

    @Subcommand("factory")
    public void factories(TowncraftPlayer player, FactoryType.Factory factoryType) {
        Towncraft.getViewFrame().open(FactoryMenu.class, player, factoryType.key());
    }
}
