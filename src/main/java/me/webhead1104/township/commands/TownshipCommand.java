package me.webhead1104.township.commands;

import com.google.gson.GsonBuilder;
import me.webhead1104.township.InstantAdapter;
import me.webhead1104.township.Township;
import me.webhead1104.township.commands.arguments.AnimalTypeArgument;
import me.webhead1104.township.commands.arguments.FactoryTypeArgument;
import me.webhead1104.township.commands.arguments.ItemTypeArgument;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import net.strokkur.commands.annotations.Command;
import net.strokkur.commands.annotations.Description;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Executor;
import net.strokkur.commands.annotations.arguments.CustomArg;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;

@Command("township")
@Description("The main Township command.")
public final class TownshipCommand {

    @Executes
    void execute(CommandSender ignored, @Executor Player player) {
        if (!player.getInventory().isEmpty()) Township.getInventoryManager().addPlayerInventory(player);
        player.getInventory().clear();
        Township.getWorldManager().openWorldMenu(player);
    }

    @Executes("item get")
    void getItem(CommandSender ignored, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType itemType) {
        ItemCommand.getItem(player, itemType);
    }

    @Executes("item set")
    void setItem(CommandSender ignored, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType itemType, int amount) {
        ItemCommand.setItem(player, amount, itemType);
    }

    @Executes("item add")
    void addItem(CommandSender ignored, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType itemType, int amount) {
        ItemCommand.addItem(player, amount, itemType);
    }

    @Executes("item remove")
    void removeItem(CommandSender ignored, @Executor Player player, @CustomArg(ItemTypeArgument.class) ItemType itemType, int amount) {
        ItemCommand.removeItem(player, amount, itemType);
    }

    @Executes("animal")
    void animals(CommandSender ignored, @Executor Player player, @CustomArg(AnimalTypeArgument.class) AnimalType animalType) {
        Township.getAnimalsManager().openAnimalMenu(player, animalType);
    }

    @Executes("factory")
    void factories(CommandSender ignored, @Executor Player player, @CustomArg(FactoryTypeArgument.class) FactoryType factoryType) {
        Township.getFactoriesManager().openFactoryMenu(player, factoryType);
    }

    @Executes("cash")
    void cash(CommandSender ignored, @Executor Player player) {
        CashCommand.getCash(player);
    }

    @Executes("cash add")
    void addCash(CommandSender ignored, @Executor Player player, int amount) {
        CashCommand.addCash(player, amount);
    }

    @Executes("cash remove")
    void removeCash(CommandSender ignored, @Executor Player player, int amount) {
        CashCommand.removeCash(player, amount);
    }

    @Executes("cash set")
    void setCash(CommandSender ignored, @Executor Player player, int amount) {
        CashCommand.setCash(player, amount);
    }

    @Executes("coins")
    void coins(CommandSender ignored, @Executor Player player) {
        CoinsCommand.getCoins(player);
    }

    @Executes("coins add")
    void addCoins(CommandSender ignored, @Executor Player player, int amount) {
        CoinsCommand.addCoins(player, amount);
    }

    @Executes("coins remove")
    void removeCoins(CommandSender ignored, @Executor Player player, int amount) {
        CoinsCommand.removeCoins(player, amount);
    }

    @Executes("coins set")
    void setCoins(CommandSender ignored, @Executor Player player, int amount) {
        CoinsCommand.setCoins(player, amount);
    }

    @Executes("level")
    void level(CommandSender ignored, @Executor Player player) {
        LevelCommand.getLevel(player);
    }

    @Executes("level add")
    void addLevel(CommandSender ignored, @Executor Player player, int amount) {
        LevelCommand.addLevels(player, amount);
    }

    @Executes("level remove")
    void removeLevel(CommandSender ignored, @Executor Player player, int amount) {
        LevelCommand.removeLevels(player, amount);
    }

    @Executes("level set")
    void setLevel(CommandSender ignored, @Executor Player player, int amount) {
        LevelCommand.setLevel(player, amount);
    }

    @Executes("population")
    void population(CommandSender ignored, @Executor Player player) {
        PopulationCommand.getPopulation(player);
    }

    @Executes("population add")
    void addPopulation(CommandSender ignored, @Executor Player player, int amount) {
        PopulationCommand.addPopulation(player, amount);
    }

    @Executes("population remove")
    void removePopulation(CommandSender ignored, @Executor Player player, int amount) {
        PopulationCommand.removePopulation(player, amount);
    }

    @Executes("population set")
    void setPopulation(CommandSender ignored, @Executor Player player, int amount) {
        PopulationCommand.setPopulation(player, amount);
    }

    @Executes("xp")
    void xp(CommandSender ignored, @Executor Player player) {
        XpCommand.getXp(player);
    }

    @Executes("xp add")
    void addXp(CommandSender ignored, @Executor Player player, int amount) {
        XpCommand.addXp(player, amount);
    }

    @Executes("xp remove")
    void removeXp(CommandSender ignored, @Executor Player player, int amount) {
        XpCommand.removeXp(player, amount);
    }

    @Executes("xp set")
    void setXp(CommandSender ignored, @Executor Player player, int amount) {
        XpCommand.setXp(player, amount);
    }

    @Executes("lock animal")
    void lock(CommandSender ignored, @Executor Player player, @CustomArg(AnimalTypeArgument.class) AnimalType type, boolean locked) {
        LockCommand.lock(player, type, locked);
    }

    @Executes("lock factory")
    void lockFactory(CommandSender ignored, @Executor Player player, @CustomArg(FactoryTypeArgument.class) FactoryType type, boolean locked) {
        LockCommand.lock(player, type, locked);
    }

    @Executes("lock trains")
    void lockTrains(CommandSender ignored, @Executor Player player, boolean locked) {
        LockCommand.lockTrains(player, locked);
    }

    @Executes("lock train")
    void lockTrain(CommandSender ignored, @Executor Player player, int train, boolean locked) {
        LockCommand.lockTrain(player, train, locked);
    }
}
