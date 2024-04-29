package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.flame.menus.items.MenuItem;
import me.flame.menus.menu.PaginatedMenu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.AnimalsEnum;
import me.webhead1104.township.data.enums.FactoriesEnum;
import me.webhead1104.township.utils.Items;
import me.webhead1104.township.utils.Utils;
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
            if (Database.getPlayerData(player, "townName").equals("none")) {
                plugin.getWorldManager().townName(player);
            } else plugin.getWorldManager().getWorld(player, 0);
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
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
                                                    Database.setPlayerData(player, "townName", value);
                                                    return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                                                        try {
                                                            getWorld(stateSnapshot.getPlayer(), 0);
                                                        } catch (Exception e) {
                                                            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
                                                        }
                                                    }));
                                                } else
                                                    return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "That name is taken!"));
                                            } else
                                                return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "Please try again"));
                                        } catch (Exception e) {
                                            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
                                        }
                                    }
                                } else
                                    return List.of(AnvilGUI.ResponseAction.replaceInputText(ChatColor.RED + "Please try again"));
                                return Collections.emptyList();
                            }
                    ).open(player);
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    //world loading
    public void getWorld(Player player, int page) {
        try {
            worldMenu.getPage(page);
            factories(player, page);
            animals(player, page);
            int level = Integer.parseInt(Database.getPlayerData(player, "level"));
            int population = Integer.parseInt(Database.getPlayerData(player, "population"));
            int coins = Integer.parseInt(Database.getPlayerData(player, "coins"));
            int cash = Integer.parseInt(Database.getPlayerData(player, "cash"));
            String townName = Database.getPlayerData(player, "townName");
            worldMenu.getFiller().fill(Items.grass);
            player.getInventory().setItem(13, Items.arrowUP.getItemStack());
            player.getInventory().setItem(21, Items.arrowLEFT.getItemStack());
            player.getInventory().setItem(23, Items.arrowRIGHT.getItemStack());
            player.getInventory().setItem(31, Items.arrowDOWN.getItemStack());
            player.getInventory().setItem(22, Items.profile.editor().setName(ChatColor.GREEN + townName).done().getItemStack());
            player.getInventory().setItem(9, Items.levelAndPop.editor().setName(ChatColor.AQUA + "Level " + level).setLore(ChatColor.RED + "Population " + population).done().getItemStack());
            player.getInventory().setItem(17, Items.coinsAndCash.editor().setName(ChatColor.YELLOW + "Coins " + coins).setLore(ChatColor.GREEN + "Cash " + cash).done().getItemStack());
            worldMenu.open(player, page);
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public void factories(Player player, int menuPage) {
        try {
            PaginatedMenu menu = worldMenu;
            menu.getPage(menuPage);
            JsonObject obj = new Gson().fromJson(Database.getWorldData(player, "factories"), JsonObject.class);
            for (int i = 1; i < obj.size() + 1; ++i) {
                FactoriesEnum Enum = Utils.switchToFactory(i);
                int Slot = 0;
                int Page = 0;
                boolean isNone = false;
                MenuItem menuItem = null;
                JsonObject object = obj.get(Enum.getID().toLowerCase()).getAsJsonObject();
                String page = object.get("page").getAsString();
                String slot = object.get("slot").getAsString();
                if (!slot.equals("none") && !page.equals("none")) {
                    Slot = Integer.parseInt(slot);
                    Page = Integer.parseInt(page);
                    menuItem = Enum.getMenuItem();
                } else isNone = true;
                if (!isNone) {
                    if (Page == menuPage) {
                        menu.setItem(Slot, menuItem);
                        menu.setItem(Slot + 1, menuItem);
                        menu.setItem(Slot + 9, menuItem);
                        menu.setItem(Slot + 10, menuItem);
                    }
                }
            }
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }

    public void animals(Player player, int menuPage) {
        try {
            PaginatedMenu menu = worldMenu;
            menu.getPage(menuPage);
            String string = Database.getWorldData(player, "animals");
            JsonObject obj = new Gson().fromJson(string, JsonObject.class);
            for (int i = 1; i < obj.size() + 1; ++i) {
                AnimalsEnum Enum = Utils.switchToAnimal(i);
                int Slot = 0;
                int Page = 0;
                boolean isNone = false;
                MenuItem menuItem = null;
                JsonObject object = obj.get(Enum.getID().toLowerCase()).getAsJsonObject();
                String page = object.get("page").getAsString();
                String slot = object.get("slot").getAsString();
                if (!slot.equals("none") && !page.equals("none")) {
                    Slot = Integer.parseInt(slot);
                    Page = Integer.parseInt(page);
                    menuItem = Enum.getMenuItem();
                } else isNone = true;
                if (!isNone)
                    if (Page == menuPage) {
                        menu.setItem(Slot, menuItem);
                        menu.setItem(Slot + 1, menuItem);
                        menu.setItem(Slot + 9, menuItem);
                        menu.setItem(Slot + 10, menuItem);
                    }
            }
        } catch (Exception e) {plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));}
    }
}