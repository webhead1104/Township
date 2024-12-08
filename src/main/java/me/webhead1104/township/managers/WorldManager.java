package me.webhead1104.township.managers;

import lombok.AllArgsConstructor;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.enums.WorldTileType;
import me.webhead1104.township.data.objects.Expansion;
import me.webhead1104.township.data.objects.Plot;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.objects.World;
import me.webhead1104.township.utils.ItemBuilder;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Msg;
import me.webhead1104.township.utils.Utils;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@AllArgsConstructor
public class WorldManager {
    private final Township plugin;

    public void load(Player player) {
        if (Township.getUserManager().getUser(player.getUniqueId()).getTownName().equals("none")) {
            townName(player);
            return;
        }
        openWorldMenu(player);
    }

    public void townName(Player player) {
        ItemBuilder builder = new ItemBuilder(Material.PAPER, "town_name_paper")
                .displayName(Msg.format("<green>Town Name"))
                .lore(Msg.format("<red>you cannot change this once you set it!"));
        new AnvilGUI.Builder()
                .preventClose()
                .plugin(plugin)
                .jsonTitle(Msg.formatToJson("<gold>Set Your Town name!"))
                .itemLeft(builder.build()).onClick((slot, stateSnapshot) -> {
                    Player p = stateSnapshot.getPlayer();
                    if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
                    if (!stateSnapshot.getText().isEmpty()) {
                        String value = stateSnapshot.getText();
                        if (!value.contains("That name is taken!") && !value.contains("Town Name")) {
                            List<String> townNames;
                            try {
                                townNames = Township.getDatabase().getTakenTownNames().get();
                            } catch (InterruptedException | ExecutionException e) {
                                throw new RuntimeException(e);
                            }
                            if (!townNames.contains(value)) {
                                Township.getUserManager().getUser(p.getUniqueId()).setTownName(value);
                                return List.of(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> openWorldMenu(stateSnapshot.getPlayer())));
                            } else {
                                return List.of(AnvilGUI.ResponseAction.replaceInputText(Msg.formatToJson("<red>That name is taken!")));
                            }
                        }
                    }
                    return Collections.emptyList();
                }).open(player);
    }

    public Inventory getWorld(Player player, int section) {
        player.setItemOnCursor(ItemStack.empty());
        User user = Township.getUserManager().getUser(player.getUniqueId());
        user.setSection(section);
        World world = user.getWorld();
        Inventory inventory = Bukkit.createInventory(null, 54, Msg.format("World Menu"));
        world.getSection(section).getSlotMap().forEach((key, value) -> {
            ItemBuilder builder = new ItemBuilder(value.getTileType().getItem());
            if (value.getPlot() != null) {
                Plot plot = value.getPlot();
                builder.pdcSetString(ItemBuilder.plotDataKey, plot.toString());
                builder.id(plot.getPlotType().getId());
                builder.material(plot.getPlotType().getItemType().getItemStack().getType());
                builder.displayName(new ItemBuilder(plot.getPlotType().getItemType().getItemStack()).getDisplayName());
            }
            if (value.getExpansion() != null) {
                Expansion expansion = value.getExpansion();
                builder.pdcSetString(ItemBuilder.expansionDataKey, expansion.toString());
                builder.id("expansion");
                builder.material(Material.PODZOL);
                builder.displayName(Msg.format("Expansion"));
                builder.lore(List.of(Msg.format("<aqua>Click to open the expansion menu!")));
            }
            if (value.getTileType().equals(WorldTileType.TRAIN) && !(user.getLevel().getLevel() >= 5)) {
                builder.lore(List.of(Msg.format("<red>You need to be level <aqua>5 <red>to access the trains!"),
                        Msg.format("<red>You are level <aqua>" + user.getLevel().getLevel())));
            }
            if (value.getTileType().getAnimalType() != null && !(user.getLevel().getLevel() >= value.getTileType().getAnimalType().getLevelNeeded())) {
                builder.lore(List.of(Msg.format("<red>You need to be level <aqua>5 <red>to access the " + Utils.thing2(value.getTileType().getAnimalType().getID()) + "!"),
                        Msg.format("<red>You are level <aqua>" + user.getLevel().getLevel())));
            }
            inventory.setItem(key, builder.build());
        });
        player.getInventory().clear();
        switch (section) {
            case 0 -> {
                player.getInventory().setItem(23, MenuItems.arrowRight);
                player.getInventory().setItem(31, MenuItems.arrowDown);
            }
            case 1, 2, 3, 4, 5, 6 -> {
                player.getInventory().setItem(21, MenuItems.arrowLeft);
                player.getInventory().setItem(23, MenuItems.arrowRight);
                player.getInventory().setItem(31, MenuItems.arrowDown);
            }
            case 7 -> {
                player.getInventory().setItem(21, MenuItems.arrowLeft);
                player.getInventory().setItem(31, MenuItems.arrowDown);
            }
            case 15, 23, 31, 39, 47, 55 -> {
                player.getInventory().setItem(21, MenuItems.arrowLeft);
                player.getInventory().setItem(31, MenuItems.arrowDown);
                player.getInventory().setItem(13, MenuItems.arrowUp);
            }
            case 63 -> {
                player.getInventory().setItem(13, MenuItems.arrowUp);
                player.getInventory().setItem(21, MenuItems.arrowLeft);
            }
            case 62, 61, 60, 59, 58, 57 -> {
                player.getInventory().setItem(21, MenuItems.arrowLeft);
                player.getInventory().setItem(23, MenuItems.arrowRight);
                player.getInventory().setItem(13, MenuItems.arrowUp);
            }
            case 8, 16, 24, 32, 40, 48 -> {
                player.getInventory().setItem(23, MenuItems.arrowRight);
                player.getInventory().setItem(13, MenuItems.arrowUp);
                player.getInventory().setItem(31, MenuItems.arrowDown);
            }
            case 56 -> {
                player.getInventory().setItem(13, MenuItems.arrowUp);
                player.getInventory().setItem(23, MenuItems.arrowRight);
            }
            default -> {
                player.getInventory().setItem(21, MenuItems.arrowLeft);
                player.getInventory().setItem(23, MenuItems.arrowRight);
                player.getInventory().setItem(13, MenuItems.arrowUp);
                player.getInventory().setItem(31, MenuItems.arrowDown);
            }
        }
        ItemBuilder profile = new ItemBuilder(MenuItems.profile).displayName(Msg.format("<green>" + user.getTownName()));
        player.getInventory().setItem(22, profile.build());
        ItemBuilder levelAndPop = new ItemBuilder(MenuItems.levelAndPop);
        levelAndPop.displayName(Msg.format("<aqua>Level " + user.getLevel().getLevel()));
        if (!((user.getLevel().getLevel() + 1) == Township.getLevelManager().getLevelList().size())) {
            levelAndPop.lore(List.of(Msg.format("<aqua>Xp " + user.getLevel().getXp()),
                    Msg.format(user.getLevel().getProgressBar()), Msg.format("<red>Population " + user.getPopulation())));
        } else {
            levelAndPop.lore(List.of(Msg.format("<dark_red>You have reached the max level!"),
                    Msg.format("<red>Population " + user.getPopulation())));
        }
        player.getInventory().setItem(9, levelAndPop.build());
        ItemBuilder coinsAndCash = new ItemBuilder(MenuItems.coinsAndCash)
                .displayName(Msg.format("<yellow>Coins " + user.getCoins()))
                .lore(List.of(Msg.format("<green>Cash " + user.getCash())));
        player.getInventory().setItem(17, coinsAndCash.build());
        return inventory;
    }

    public Inventory getWorld(Player player) {
        return getWorld(player, Township.getUserManager().getUser(player.getUniqueId()).getSection());
    }

    public void openWorldMenu(Player player, int section) {
        player.openInventory(getWorld(player, section));
    }

    public void openWorldMenu(Player player) {
        player.openInventory(getWorld(player, Township.getUserManager().getUser(player.getUniqueId()).getSection()));
    }
}
