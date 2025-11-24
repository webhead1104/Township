package me.webhead1104.towncraft.commands;

import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.features.world.WorldUtils;
import studio.mevera.imperat.annotations.Command;
import studio.mevera.imperat.annotations.Description;
import studio.mevera.imperat.annotations.Usage;

@Command("towncraft")
@Description("The main Towncraft command")
//@ExternalSubCommand({
//        CashCommand.class, CoinsCommand.class, ItemCommand.class, LevelCommand.class,
//        LockCommand.class, MaxPopulationCommand.class, PopulationCommand.class, XpCommand.class
//})
public class TowncraftCommand {
    @Usage
    public void execute(TowncraftPlayer player) {
        System.out.println("HET!!!!!!!!!!!!!!");
        //todo
//        Towncraft.getInventoryManager().addPlayerInventory(player);
        player.getInventory().clear();
        WorldUtils.openWorldMenu(player);
    }
//todo
//    @SubCommand("animal")
//    void animals(TowncraftPlayer player, @CustomArg(AnimalTypeArgument.class) AnimalType.Animal animalType) {
//        Towncraft.getViewFrame().open(AnimalMenu.class, player, animalType.key());
//    }
//
//    @SubCommand("factory")
//    void factories(CommandSender $, @Executor Player player, @CustomArg(FactoryTypeArgument.class) AnimalType.Animal factoryType) {
//        Towncraft.getViewFrame().open(FactoryMenu.class, player, factoryType.key());
//    }
}
