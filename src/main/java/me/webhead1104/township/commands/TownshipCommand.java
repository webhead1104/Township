package me.webhead1104.township.commands;

import lombok.NoArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.objects.Trains;
import me.webhead1104.township.data.objects.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static me.webhead1104.township.utils.MiniMessageTemplate.MM;

@NoArgsConstructor
public class TownshipCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                if (!player.getInventory().isEmpty()) Township.getInventoryManager().addPlayerInventory(player);
                player.getInventory().clear();
                Township.getWorldManager().load(player);
                return true;
            }
            switch (args[0].toLowerCase()) {
                case "animals" -> {
                    for (AnimalType type : AnimalType.values()) {
                        if (type.equals(AnimalType.valueOf(args[1].toUpperCase()))) {
                            Township.getAnimalsManager().openAnimalMenu(player, type);
                        }
                    }
                    return true;
                }
                case "factories" -> {
                    for (FactoryType type : FactoryType.values()) {
                        if (type.equals(FactoryType.valueOf(args[1].toUpperCase()))) {
                            Township.getFactoriesManager().openFactoryMenu(player, type);
                        }
                    }
                    return true;
                }
                case "get_section" -> {
                    sender.sendMessage(String.valueOf(Township.getUserManager().getUser(player.getUniqueId()).getSection()));
                    return true;
                }
                case "get_items_back" -> Township.getInventoryManager().returnItemsToPlayer(player);
                case "test2" ->
                        Township.getUserManager().getUser(player.getUniqueId()).getTrains().getTrain(1).setInStation(true);
                case "test3" ->
                        Township.getUserManager().getUser(player.getUniqueId()).getTrains().getTrain(1).setClaimItems(true);
                case "test4" -> {
                    Township.getUserManager().getUser(player.getUniqueId()).getTrains().setTrain(Trains.Train.createTrain(1), 1);
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("set_section")) {
                    try {
                        int section = Integer.parseInt(args[1]);
                        Township.getUserManager().getUser(player.getUniqueId()).setSection(section);
                        sender.sendMessage(MM."Section set to \{section}");
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MM."Invalid section number.");
                    }
                    return true;
                }
            }
            switch (args[0].toLowerCase()) {
                case "population" -> {
                    try {
                        int amount = Integer.parseInt(args[2]);
                        User user = Township.getUserManager().getUser(player.getUniqueId());
                        switch (args[1].toLowerCase()) {
                            case "add" -> {
                                user.setPopulation(user.getPopulation() + amount);
                                sender.sendMessage(MM."added \{amount} population");
                            }
                            case "remove" -> {
                                user.setPopulation(user.getPopulation() - amount);
                                sender.sendMessage(MM."removed \{amount} population");
                            }
                            case "get" -> player.sendMessage(MM."population = \{user.getPopulation()}");
                            case "set" -> {
                                user.setPopulation(amount);
                                sender.sendMessage(MM."set population to \{amount}");
                            }
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MM."Invalid amount.");
                    }
                }
                case "level" -> {
                    try {
                        int amount = Integer.parseInt(args[2]);
                        User user = Township.getUserManager().getUser(player.getUniqueId());
                        switch (args[1].toLowerCase()) {
                            case "add" -> {
                                user.setLevel(user.getLevel() + amount);
                                sender.sendMessage(MM."added \{amount} level");
                            }
                            case "remove" -> {
                                user.setLevel(user.getLevel() - amount);
                                sender.sendMessage(MM."removed \{amount} level");
                            }
                            case "get" -> player.sendMessage(MM."level = \{user.getLevel()}");
                            case "set" -> {
                                user.setLevel(amount);
                                sender.sendMessage(MM."set level to \{amount}");
                            }
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MM."Invalid amount.");
                    }
                }
                case "coins" -> {
                    try {
                        int amount = Integer.parseInt(args[2]);
                        User user = Township.getUserManager().getUser(player.getUniqueId());
                        switch (args[1].toLowerCase()) {
                            case "add" -> {
                                user.setCoins(user.getCoins() + amount);
                                sender.sendMessage(MM."added \{amount} coins");
                            }
                            case "remove" -> {
                                user.setCoins(user.getCoins() - amount);
                                sender.sendMessage(MM."removed \{amount} coins");
                            }
                            case "get" -> player.sendMessage(MM."coins = \{user.getCoins()}");
                            case "set" -> {
                                user.setCoins(amount);
                                sender.sendMessage(MM."set coins to \{amount}");
                            }
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MM."Invalid amount.");
                    }
                }
                case "cash" -> {
                    try {
                        int amount = Integer.parseInt(args[2]);
                        User user = Township.getUserManager().getUser(player.getUniqueId());
                        switch (args[1].toLowerCase()) {
                            case "add" -> {
                                user.setCash(user.getCash() + amount);
                                sender.sendMessage(MM."added \{amount} cash");
                            }
                            case "remove" -> {
                                user.setCash(user.getCash() - amount);
                                sender.sendMessage(MM."removed \{amount} cash");
                            }
                            case "get" -> player.sendMessage(MM."cash = \{user.getCash()}");
                            case "set" -> {
                                user.setCash(amount);
                                sender.sendMessage(MM."set cash to \{amount}");
                            }
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(MM."Invalid amount.");
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            result.addAll(List.of("animals", "factories", "get_section", "set_section", "get_items_back"));
        } else if (args.length == 2) {
            switch (args[0]) {
                case "animals" -> {
                    List<String> list = new ArrayList<>();
                    for (AnimalType type : AnimalType.values()) {
                        list.add(type.name());
                    }
                    result.addAll(list);
                }
                case "factories" -> {
                    List<String> list = new ArrayList<>();
                    for (FactoryType type : FactoryType.values()) {
                        list.add(type.name());
                    }
                    result.addAll(list);
                }
            }
        }
        return result;
    }
}
