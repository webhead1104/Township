package me.webhead1104.township;

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
                                                    plugin.getDatabase().setPlayerData(player,"townName",value);
                                                    return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                                                        try {
                                                            getWorld(stateSnapshot.getPlayer(), 0);
                                                        } catch (Exception e) {
                                                            plugin.getLogger().log(Level.SEVERE, "error " +e);
                                                        }
                                                    }));
                                                } else return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "That name is taken!"));
                                            } else return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "Please try again"));
                                        } catch (Exception e) {
                                            plugin.getLogger().log(Level.SEVERE, "ERROR " + e);
                                        }
                                    }
                                } else return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "Please try again"));
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
            int level = Integer.parseInt(plugin.getDatabase().getPlayerData(player,"level"));
            int population = Integer.parseInt(plugin.getDatabase().getPlayerData(player,"population"));
            int coins = Integer.parseInt(plugin.getDatabase().getPlayerData(player,"coins"));
            int cash = Integer.parseInt(plugin.getDatabase().getPlayerData(player,"cash"));
            String townName = plugin.getDatabase().getPlayerData(player,"townName");
            worldMenu.getFiller().fill(plugin.getItems().grass);
            player.getInventory().setItem(13, plugin.getItems().arrowUP.getItemStack());
            player.getInventory().setItem(21, plugin.getItems().arrowLEFT.getItemStack());
            player.getInventory().setItem(23, plugin.getItems().arrowRIGHT.getItemStack());
            player.getInventory().setItem(31, plugin.getItems().arrowDOWN.getItemStack());
            player.getInventory().setItem(22, plugin.getItems().profile.editor().setName(ChatColor.WHITE + townName).done().getItemStack());
            player.getInventory().setItem(9, plugin.getItems().levelAndPop.editor().setName(ChatColor.AQUA + "Level " + level).setLore(ChatColor.RED + "Population " + population).done().getItemStack());
            player.getInventory().setItem(17, plugin.getItems().coinsAndCash.editor().setName(ChatColor.YELLOW + "Coins " + coins).setLore(ChatColor.GREEN + "Cash " + cash).done().getItemStack());
            worldMenu.open(player, page);
        }catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " +e);
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
                    String input = plugin.getDatabase().getWorldData(player,"plottype");
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
                        plugin.getDatabase().setWorldata(player,"plottype",value);
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
            String factorypage = plugin.getDatabase().getWorldData(player,"factorypage");
            String factoryslot = plugin.getDatabase().getWorldData(player,"factoryslot");
            //pages
            int feedmillpage = Integer.parseInt(factorypage.substring(factorypage.indexOf("feedmill") + 9, factorypage.indexOf("bakery") - 1));
            int bakerypage = Integer.parseInt(factorypage.substring(factorypage.indexOf("bakery") + 7, factorypage.indexOf("dairy") - 1));
            int dairypage = Integer.parseInt(factorypage.substring(factorypage.indexOf("dairy") + 6, factorypage.indexOf("sugar") - 1));
            int sugarpage = Integer.parseInt(factorypage.substring(factorypage.indexOf("sugar") + 6, factorypage.indexOf("end") - 1));
            //slots
            int feedmillslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("feedmill") + 9, factoryslot.indexOf("bakery") - 1)),
                    bakeryslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("bakery") + 7, factoryslot.indexOf("dairy") - 1)),
                    dairyslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("dairy") + 6, factoryslot.indexOf("sugar") - 1)),
                    sugarslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("sugar") + 6, factoryslot.indexOf("end") - 1));
            if (feedmillpage == page) {
                if (feedmillslot != 0) {
                    menu.setItem(feedmillslot, plugin.getItems().feedmillTL);
                    menu.setItem(feedmillslot + 1, plugin.getItems().feedmillTR);
                    menu.setItem(feedmillslot + 9, plugin.getItems().feedmillBL);
                    menu.setItem(feedmillslot + 10, plugin.getItems().feedmillBR);
                }
            }
            if (bakerypage == page) {
                if (bakeryslot != 0) {
                    menu.setItem(bakeryslot, plugin.getItems().bakeryTL);
                    menu.setItem(bakeryslot + 1, plugin.getItems().bakeryTR);
                    menu.setItem(bakeryslot + 9, plugin.getItems().bakeryBL);
                    menu.setItem(bakeryslot + 10, plugin.getItems().bakeryBR);
                }
            }
            if (dairypage == page) {
                if (dairyslot != 0) {
                    menu.setItem(dairyslot, plugin.getItems().dairyTL);
                    menu.setItem(dairyslot + 1, plugin.getItems().dairyTR);
                    menu.setItem(dairyslot + 9, plugin.getItems().dairyBL);
                    menu.setItem(dairyslot + 10, plugin.getItems().dairyBR);
                }
            }
            if (sugarpage == page) {
                if (sugarslot != 0) {
                    menu.setItem(sugarslot, plugin.getItems().sugarTL);
                    menu.setItem(sugarslot + 1, plugin.getItems().sugarTR);
                    menu.setItem(sugarslot + 9, plugin.getItems().sugarBL);
                    menu.setItem(sugarslot + 10, plugin.getItems().sugarBR);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " +e);
        }
    }

    public void animals(PaginatedMenu menu, Player player, int page) {
        try {
            menu.getPage(page);
            String factorypage = plugin.getDatabase().getWorldData(player,"factorypage");
            String factoryslot = plugin.getDatabase().getWorldData(player,"factoryslot");
            //pages
            int cowshedpage = Integer.parseInt(factorypage.substring(factorypage.indexOf("cowshed") + 8, factorypage.indexOf("chickencoop") - 1));
            int chickencooppage = Integer.parseInt(factorypage.substring(factorypage.indexOf("chickencoop") + 12, factorypage.indexOf("sheepfarm") - 1));
            int sheepfarmpage = Integer.parseInt(factorypage.substring(factorypage.indexOf("sheepfarm") + 10, factorypage.indexOf("pigfarm") - 1));
            int pigfarmpage = Integer.parseInt(factorypage.substring(factorypage.indexOf("pigfarm") + 8, factorypage.indexOf("end") - 1));
            //slots
            int cowshedslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("cowshed") + 8, factoryslot.indexOf("chickencoop") - 1)),
                    chickencoopslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("chickencoop") + 12, factoryslot.indexOf("sheepfarm") - 1)),
                    sheepfarmslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("sheepfarm") + 10, factoryslot.indexOf("pigfarm") - 1)),
                    pigfarmslot = Integer.parseInt(factoryslot.substring(factoryslot.indexOf("pigfarm") + 8, factoryslot.indexOf("end") - 1));
            if (cowshedpage == page) {
                if (cowshedslot != 0) {
                    menu.setItem(cowshedslot, plugin.getItems().cowshedTL);
                    menu.setItem(cowshedslot + 1, plugin.getItems().cowshedTR);
                    menu.setItem(cowshedslot + 9, plugin.getItems().cowshedBL);
                    menu.setItem(cowshedslot + 10, plugin.getItems().cowshedBR);
                }
            }
            if (chickencooppage == page) {
                if (chickencoopslot != 0) {
                    menu.setItem(chickencoopslot, plugin.getItems().chickenTL);
                    menu.setItem(chickencoopslot + 1, plugin.getItems().chickenTR);
                    menu.setItem(chickencoopslot + 9, plugin.getItems().chickenBL);
                    menu.setItem(chickencoopslot + 10, plugin.getItems().chickenBR);
                }
            }
            if (sheepfarmpage == page) {
                if (sheepfarmslot != 0) {
                    menu.setItem(sheepfarmslot, plugin.getItems().sheepTL);
                    menu.setItem(sheepfarmslot + 1, plugin.getItems().sheepTR);
                    menu.setItem(sheepfarmslot + 9, plugin.getItems().sheepBL);
                    menu.setItem(sheepfarmslot + 10, plugin.getItems().sheepBR);
                }
            }
            if (pigfarmpage == page) {
                if (pigfarmpage != 0) {
                    menu.setItem(pigfarmslot, plugin.getItems().pigTL);
                    menu.setItem(pigfarmslot + 1, plugin.getItems().pigTR);
                    menu.setItem(pigfarmslot + 9, plugin.getItems().pigBL);
                    menu.setItem(pigfarmslot + 10, plugin.getItems().pigBR);
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " +e);
        }
    }

    public void plots(PaginatedMenu menu, Player player, int page) {
        try {
            menu.getPage(page);
            String plotslot = plugin.getDatabase().getWorldData(player,"plotslot");
            String plotpage = plugin.getDatabase().getWorldData(player,"plotpage");
            for (int i = 1; i < 11; i++) {
                player.sendMessage("it is " + i);
                String start = "plot" + i;
                String end = "plot" + i++;
                player.sendMessage("after start and end");
                if (i == 11) {
                    end = "end";
                }
                int howlong;
                player.sendMessage("after howlong");
                if (start.equals("plot10")) {
                    howlong = 7;
                } else {
                    howlong = 6;
                }
                player.sendMessage("before pasre");
                try {
                    int plotpagefor = Integer.parseInt(splitString(plotpage, start, end, howlong));
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "error" + e);
                }
                player.sendMessage("pages done");

                String startSlot = "plot" + i;
                String endSlot = "plot" + i++;
                if (i == 11) {
                    endSlot = "end";
                }
                int howlongSlot;
                if (startSlot.equals("plot10")) {
                    howlongSlot = 7;
                } else {
                    howlongSlot = 6;
                }
                int plotSlotfFor = Integer.parseInt(splitString(plotslot, startSlot, endSlot, howlongSlot));
                // if (plotpagefor == page) menu.setItem(plotSlotfFor, Items.plot.editor().setCustomModelData(i).done());
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error " +e);
        }
    }

    public String splitString(String thingtosplit, String start, String end, int howlong) {
        String thing = null;
        try {
            thing = thingtosplit.substring(thingtosplit.indexOf(start) + howlong, thingtosplit.indexOf(end) - 1);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "error in split string " + e);
        }
        plugin.getLogger().log(Level.SEVERE, "got to this");
        return thing;
    }
}