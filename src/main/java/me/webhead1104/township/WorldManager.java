package me.webhead1104.township;

import me.flame.menus.items.MenuItem;
import me.flame.menus.menu.PaginatedMenu;
import me.flame.menus.modifiers.Modifier;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;
import java.util.logging.Level;

public class WorldManager {

    Township plugin;
    public PaginatedMenu worldMenu = PaginatedMenu.create(ChatColor.AQUA + "World", 5, 10, EnumSet.allOf(Modifier.class));

    public WorldManager(Township plugin) {
        this.plugin = plugin;
    }

    public void load(Player player) {
        try {
            if (plugin.getDatabase().getPlayerData(player, "townName").equals("none")) {
                plugin.getWorldManager().townName(player);
            } else {
                plugin.getWorldManager().getWorld(player, 0);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR  in load!!!!! " + e);
        }
    }

    public void townName(Player player) {
        try {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GREEN + "Town Name");
            itemMeta.setLore(List.of(ChatColor.RED + "you cannot change this once you set it!"));
            itemStack.setItemMeta(itemMeta);
            new AnvilGUI.Builder()
                    .plugin(plugin)
                    .preventClose()
                    .title(ChatColor.GOLD + "Set Your Town name!")
                    .itemLeft(itemStack)
                    .onClick((slot, stateSnapshot) -> {
                                if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
                                if (stateSnapshot.getText() != null) {
                                    String value = stateSnapshot.getText();
                                    if (!value.contains(ChatColor.RED + "That name is taken!") && !value.contains(ChatColor.GREEN + "Town Name")) {
                                        try {
                                            List<String> townNames = plugin.getConfig().getStringList("townNames");
                                            List<String> bannedTownNames = plugin.getConfig().getStringList("bannedTownNames");
                                            if (!bannedTownNames.contains(value)) {
                                                if (!townNames.contains(value)) {
                                                    townNames.add(value);
                                                    plugin.getConfig().set("townNames", townNames);
                                                    plugin.getDatabase().setPlayerData(player, "townName", value);
                                                    return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                                                        try {
                                                            getWorld(stateSnapshot.getPlayer(), 0);
                                                        } catch (Exception e) {
                                                            plugin.getLogger().log(Level.SEVERE, "error " + e);
                                                        }
                                                    }));
                                                } else
                                                    return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "That name is taken!"));
                                            } else
                                                return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "Please try again"));
                                        } catch (Exception e) {
                                            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
                                        }
                                    }
                                } else
                                    return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "Please try again"));
                                return Collections.emptyList();
                            }
                    ).open(player);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
    }

    public void getWorld(Player player, int page) {
        try {
            worldMenu.getPage(page);
            //plots(worldMenu, player, page);
            factories(worldMenu, player, page);
            animals(worldMenu, player, page);
            int level = Integer.parseInt(plugin.getDatabase().getPlayerData(player, "level"));
            int population = Integer.parseInt(plugin.getDatabase().getPlayerData(player, "population"));
            int coins = Integer.parseInt(plugin.getDatabase().getPlayerData(player, "coins"));
            int cash = Integer.parseInt(plugin.getDatabase().getPlayerData(player, "cash"));
            String townName = plugin.getDatabase().getPlayerData(player, "townName");
            worldMenu.getFiller().fill(plugin.getItems().grass);
            player.getInventory().setItem(13, plugin.getItems().arrowUP.getItemStack());
            player.getInventory().setItem(21, plugin.getItems().arrowLEFT.getItemStack());
            player.getInventory().setItem(23, plugin.getItems().arrowRIGHT.getItemStack());
            player.getInventory().setItem(31, plugin.getItems().arrowDOWN.getItemStack());
            player.getInventory().setItem(22, plugin.getItems().profile.editor().setName(ChatColor.WHITE + townName).done().getItemStack());
            player.getInventory().setItem(9, plugin.getItems().levelAndPop.editor().setName(ChatColor.AQUA + "Level " + level).setLore(ChatColor.RED + "Population " + population).done().getItemStack());
            player.getInventory().setItem(17, plugin.getItems().coinsAndCash.editor().setName(ChatColor.YELLOW + "Coins " + coins).setLore(ChatColor.GREEN + "Cash " + cash).done().getItemStack());
            worldMenu.open(player, page);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " + e);
        }
    }

    public void plant(Player player, int slot, int page, ItemStack itemStack) {
        player.sendMessage("YOOOOOOOOOOOOOO");
        try {
            worldMenu.getPage(page);
            if (itemStack.getType().equals(Material.AIR)) {
                player.sendMessage("null");
                player.getInventory().setItem(0, plugin.getItems().plantWheat.getItemStack());
                player.getInventory().setItem(1, plugin.getItems().plantCorn.getItemStack());
                player.getInventory().setItem(2, plugin.getItems().plantCarrot.getItemStack());
                player.getInventory().setItem(3, plugin.getItems().plantSugarcane.getItemStack());
            } else {
                if (itemStack.getItemMeta() != null) {
                    String input = plugin.getDatabase().getWorldData(player, "plottype");
                    int plot1type = Integer.parseInt(input.substring(input.indexOf("plot1") + 6, input.indexOf("plot2") - 1)),
                            plot2type = Integer.parseInt(input.substring(input.indexOf("plot2") + 6, input.indexOf("plot3") - 1)),
                            plot3type = Integer.parseInt(input.substring(input.indexOf("plot3") + 6, input.indexOf("plot4") - 1)),
                            plot4type = Integer.parseInt(input.substring(input.indexOf("plot4") + 6, input.indexOf("plot5") - 1)),
                            plot5type = Integer.parseInt(input.substring(input.indexOf("plot5") + 6, input.indexOf("plot6") - 1)),
                            plot6type = Integer.parseInt(input.substring(input.indexOf("plot6") + 6, input.indexOf("plot7") - 1)),
                            plot7type = Integer.parseInt(input.substring(input.indexOf("plot7") + 6, input.indexOf("plot8") - 1)),
                            plot8type = Integer.parseInt(input.substring(input.indexOf("plot8") + 6, input.indexOf("plot9") - 1)),
                            plot9type = Integer.parseInt(input.substring(input.indexOf("plot9") + 6, input.indexOf("plot10") - 1)),
                            plot10type = Integer.parseInt(input.substring(input.indexOf("plot10") + 7, input.indexOf("end") - 1));
                    player.setItemOnCursor(new ItemStack(Material.AIR));
                    if (itemStack.getItemMeta().getDisplayName().contains(ChatColor.GOLD + "Wheat")) {
                        player.sendMessage("wheat");
                        worldMenu.getItem(slot).editor().setName(ChatColor.GOLD + "Wheat").setLore(ChatColor.GOLD + "0").done();
                        if (itemStack.getItemMeta().getCustomModelData() == 1) {
                            plot1type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 2) {
                            plot2type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 3) {
                            plot3type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 4) {
                            plot4type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 5) {
                            plot5type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 6) {
                            plot6type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 7) {
                            plot7type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 8) {
                            plot8type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 9) {
                            plot9type = 1;
                        } else if (itemStack.getItemMeta().getCustomModelData() == 10) {
                            plot10type = 1;
                        }
                        String value = "plot1 " + plot1type + " plot2 " + plot2type + " plot3 " + plot3type + " plot4 " + plot4type + " plot5 " + plot5type + " plot6 " + plot6type + " plot7 " + plot7type + " plot8 " + plot8type + " plot9 " + plot9type + " plot10 " + plot10type + " end";
                        plugin.getDatabase().setWorldata(player, "plottype", value);
                    }
                }
            }

        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
        }
    }

    public void factories(PaginatedMenu menu, Player player, int page) {
        try {
            menu.getPage(page);
            String pageSql = plugin.getDatabase().getWorldData(player, "factorypage");
            String slotSql = plugin.getDatabase().getWorldData(player, "factoryslot");

            for (int i = 0; i < 4; i++) {
                int factorySlot = 0;
                int factoryPage = 0;
                MenuItem TL = null, TR = null, BL = null, BR = null;
                switch (i) {
                    case 0 -> {
                        factorySlot = Integer.parseInt(slotSql.substring(9, slotSql.indexOf("bakery") - 1));
                        factoryPage = Integer.parseInt(pageSql.substring(9, pageSql.indexOf("bakery") - 1));
                        TL = plugin.getItems().feedmillTL;
                        TR = plugin.getItems().feedmillTR;
                        BL = plugin.getItems().feedmillBL;
                        BR = plugin.getItems().feedmillBR;
                    }
                    case 1 -> {
                        factorySlot = Integer.parseInt(slotSql.substring(slotSql.indexOf("bakery") + 7, slotSql.indexOf("dairy") - 1));
                        factoryPage = Integer.parseInt(pageSql.substring(pageSql.indexOf("bakery") + 7, pageSql.indexOf("dairy") - 1));
                        TL = plugin.getItems().bakeryTL;
                        TR = plugin.getItems().bakeryBR;
                        BL = plugin.getItems().bakeryTR;
                        BR = plugin.getItems().bakeryBL;
                    }
                    case 2 -> {
                        factorySlot = Integer.parseInt(slotSql.substring(slotSql.indexOf("dairy") + 6, slotSql.indexOf("sugar") - 1));
                        factoryPage = Integer.parseInt(pageSql.substring(pageSql.indexOf("dairy") + 6, pageSql.indexOf("sugar") - 1));
                        TL = plugin.getItems().dairyTL;
                        TR = plugin.getItems().dairyTR;
                        BL = plugin.getItems().dairyBL;
                        BR = plugin.getItems().dairyBR;
                    }
                    case 3 -> {
                        factorySlot = Integer.parseInt(slotSql.substring(slotSql.indexOf("sugar") + 6, slotSql.indexOf("end") - 1));
                        factoryPage = Integer.parseInt(pageSql.substring(pageSql.indexOf("sugar") + 6, pageSql.indexOf("end") - 1));
                        TL = plugin.getItems().sugarTL;
                        TR = plugin.getItems().sugarTR;
                        BL = plugin.getItems().sugarBL;
                        BR = plugin.getItems().sugarBR;
                    }
                }
                if (factoryPage == page) {
                    if (factorySlot != 0) {
                        menu.setItem(factorySlot, TL);
                        menu.setItem(factorySlot + 1, TR);
                        menu.setItem(factorySlot + 9, BL);
                        menu.setItem(factorySlot + 10, BR);
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " + e);
        }
    }

    public void animals(PaginatedMenu menu, Player player, int page) {
        try {
            menu.getPage(page);
            String pageSql = plugin.getDatabase().getWorldData(player, "animalspage");
            String slotSql = plugin.getDatabase().getWorldData(player, "animalsslot");
            for (int i = 0; i < 4; i++) {
                int animalsSlot = 0;
                int animalsPage = 0;
                MenuItem TL = null, TR = null, BL = null, BR = null;
                switch (i) {
                    case 0 -> {
                        animalsSlot = Integer.parseInt(slotSql.substring(8, slotSql.indexOf("chickencoop") - 1));
                        animalsPage = Integer.parseInt(pageSql.substring(8, pageSql.indexOf("chickencoop") - 1));
                        TL = plugin.getItems().cowshedTL;
                        TR = plugin.getItems().cowshedTR;
                        BL = plugin.getItems().cowshedBL;
                        BR = plugin.getItems().cowshedBR;
                    }
                    case 1 -> {
                        animalsSlot = Integer.parseInt(slotSql.substring(slotSql.indexOf("chickencoop") + 12, slotSql.indexOf("sheepfarm") - 1));
                        animalsPage = Integer.parseInt(pageSql.substring(pageSql.indexOf("chickencoop") + 12, pageSql.indexOf("sheepfarm") - 1));
                        TL = plugin.getItems().chickenTL;
                        TR = plugin.getItems().chickenTR;
                        BL = plugin.getItems().chickenBL;
                        BR = plugin.getItems().chickenBR;
                    }
                    case 2 -> {
                        animalsSlot = Integer.parseInt(slotSql.substring(slotSql.indexOf("sheepfarm") + 10, slotSql.indexOf("pigfarm") - 1));
                        animalsPage = Integer.parseInt(pageSql.substring(pageSql.indexOf("sheepfarm") + 10, pageSql.indexOf("pigfarm") - 1));
                        TL = plugin.getItems().sheepTL;
                        TR = plugin.getItems().sheepTR;
                        BL = plugin.getItems().sheepBL;
                        BR = plugin.getItems().sheepBR;
                    }
                    case 3 -> {
                        animalsSlot = Integer.parseInt(slotSql.substring(slotSql.indexOf("pigfarm") + 8, slotSql.indexOf("end") - 1));
                        animalsPage = Integer.parseInt(pageSql.substring(pageSql.indexOf("pigfarm") + 8, pageSql.indexOf("end") - 1));
                        TL = plugin.getItems().pigTL;
                        TR = plugin.getItems().pigTR;
                        BL = plugin.getItems().pigBL;
                        BR = plugin.getItems().pigBR;
                    }
                }
                if (animalsPage == page) {
                    if (animalsSlot != 0) {
                        menu.setItem(animalsSlot, TL);
                        menu.setItem(animalsSlot + 1, TR);
                        menu.setItem(animalsSlot + 9, BL);
                        menu.setItem(animalsSlot + 10, BR);
                    }
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " + e);
        }
    }
}