package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.flame.menus.items.MenuItem;
import me.flame.menus.menu.PaginatedMenu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Utils;
import net.minestom.server.entity.Player;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldManager { 
    public PaginatedMenu worldMenu = PaginatedMenu.create(STR."\{ChatColor.AQUA}World", 5, 10, EnumSet.allOf(Modifier.class));

    public WorldManager() {}

    public void load(Player player) {
        try {
            if (Database.getData(player.getUniqueId()).get("townName").getAsString().equals("none")) {
                Township.INSTANCE.getWorldManager().townName(player);
            } else Township.INSTANCE.getWorldManager().getWorld(player);
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR ",e);}
    }

    public void townName(Player player) {
        try {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(STR."\{ChatColor.GREEN}Town Name");
            itemMeta.setLore(List.of(STR."\{ChatColor.RED}you cannot change this once you set it!"));
            itemStack.setItemMeta(itemMeta);
            new AnvilGUI.Builder()
                    .plugin(Township.INSTANCE)
                    .preventClose()
                    .title(STR."\{ChatColor.GOLD}Set Your Town name!")
                    .itemLeft(itemStack)
                    .onClick((slot, stateSnapshot) -> {
                                if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
                                if (stateSnapshot.getText() != null) {
                                    String value = stateSnapshot.getText();
                                    if (!value.contains(STR."\{ChatColor.RED}That name is taken!") && !value.contains(STR."\{ChatColor.GREEN}Town Name")) {
                                        try {
                                            List<String> townNames = Township.INSTANCE.getConfig().getStringList("townNames");
                                            List<String> bannedTownNames = Township.INSTANCE.getConfig().getStringList("bannedTownNames");
                                            if (!bannedTownNames.contains(value)) {
                                                if (!townNames.contains(value)) {
                                                    townNames.add(value);
                                                    Township.INSTANCE.getConfig().set("townNames", townNames);
                                                    JsonObject obj = Database.getData(player.getUniqueId());
                                                    obj.addProperty("townName",value);
                                                    Database.setData(player.getUniqueId(),obj.toString());
                                                    return Arrays.asList(AnvilGUI.ResponseAction.close(), AnvilGUI.ResponseAction.run(() -> {
                                                        try {
                                                            getWorld(stateSnapshot.getPlayer());
                                                        } catch (Exception e) {
                                                            Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");
                                                        }
                                                    }));
                                                } else
                                                    return List.of(AnvilGUI.ResponseAction.replaceInputText(STR."\{ChatColor.RED}That name is taken!"));
                                            } else
                                                return List.of(AnvilGUI.ResponseAction.replaceInputText(STR."\{ChatColor.RED}Please try again"));
                                        } catch (Exception e) {
                                            Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");
                                        }
                                    }
                                } else
                                    return List.of(AnvilGUI.ResponseAction.replaceInputText(STR."\{ChatColor.RED}Please try again"));
                                return Collections.emptyList();
                            }
                    ).open(player);
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");}
    }

    //world loading
    public void getWorld(Player player) {
        try {
            JsonObject obj = Database.getData(player.getUniqueId());
            int page = obj.get("lastPage").getAsInt();
            worldMenu.getPage(page);
            factories(player, page);
            animals(player, page);
            int level = obj.get("level").getAsInt();
            int population = obj.get("population").getAsInt();
            int coins = obj.get("coins").getAsInt();
            int cash = obj.get("cash").getAsInt();
            String townName = obj.get("townName").getAsString();
            worldMenu.getFiller().fill(MenuItems.grass);
            player.getInventory().setItem(13, MenuItems.arrowUP.getItemStack());
            player.getInventory().setItem(21, MenuItems.arrowLEFT.getItemStack());
            player.getInventory().setItem(23, MenuItems.arrowRIGHT.getItemStack());
            player.getInventory().setItem(31, MenuItems.arrowDOWN.getItemStack());
            player.getInventory().setItem(22, MenuItems.profile.editor().setName(ChatColor.GREEN + townName).done().getItemStack());
            player.getInventory().setItem(9, MenuItems.levelAndPop.editor().setName(STR."\{ChatColor.AQUA}Level \{level}").setLore(STR."\{ChatColor.RED}Population \{population}").done().getItemStack());
            player.getInventory().setItem(17, MenuItems.coinsAndCash.editor().setName(STR."\{ChatColor.YELLOW}Coins \{coins}").setLore(STR."\{ChatColor.GREEN}Cash \{cash}").done().getItemStack());
            worldMenu.open(player, page);
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");}
    }

    public void getWorld(Player player, int page) {
        try {
            JsonObject obj = Database.getData(player.getUniqueId());
            worldMenu.getPage(page);
            factories(player, page);
            animals(player, page);
            int level = obj.get("level").getAsInt();
            int population = obj.get("population").getAsInt();
            int coins = obj.get("coins").getAsInt();
            int cash = obj.get("cash").getAsInt();
            String townName = obj.get("townName").getAsString();
            worldMenu.getFiller().fill(MenuItems.grass);
            player.getInventory().setItem(13, MenuItems.arrowUP.getItemStack());
            player.getInventory().setItem(21, MenuItems.arrowLEFT.getItemStack());
            player.getInventory().setItem(23, MenuItems.arrowRIGHT.getItemStack());
            player.getInventory().setItem(31, MenuItems.arrowDOWN.getItemStack());
            player.getInventory().setItem(22, MenuItems.profile.editor().setName(ChatColor.GREEN + townName).done().getItemStack());
            player.getInventory().setItem(9, MenuItems.levelAndPop.editor().setName(STR."\{ChatColor.AQUA}Level \{level}").setLore(STR."\{ChatColor.RED}Population \{population}").done().getItemStack());
            player.getInventory().setItem(17, MenuItems.coinsAndCash.editor().setName(STR."\{ChatColor.YELLOW}Coins \{coins}").setLore(STR."\{ChatColor.GREEN}Cash \{cash}").done().getItemStack());
            worldMenu.open(player, page);
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");}
    }

    public void factories(Player player, int menuPage) {
        try {
            PaginatedMenu menu = worldMenu;
            menu.getPage(menuPage);
            JsonObject obj = new Gson().fromJson(Database.getData(player.getUniqueId()), JsonObject.class).get("world_data").getAsJsonObject()
                    .get("factories").getAsJsonObject();
            Logger logger = Township.INSTANCE.getLogger();
            for (int i = 1; i < obj.size() + 1; ++i) {
                FactoryType type = Utils.switchToFactory(i);
                logger.log(Level.INFO,type.getID());
                logger.log(Level.INFO, String.valueOf(i));
                logger.log(Level.INFO,obj.toString());
                int Slot = 0;
                int Page = 0;
                boolean isNone = false;
                MenuItem menuItem = null;
                //error
                JsonObject object = obj.get(type.getID().toLowerCase()).getAsJsonObject();

                String page = object.get("page").getAsString();
                String slot = object.get("slot").getAsString();
                if (!slot.equals("none") && !page.equals("none")) {
                    Slot = Integer.parseInt(slot);
                    Page = Integer.parseInt(page);
                    menuItem = type.getMenuItem();
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
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");}
    }

    public void animals(Player player, int menuPage) {
        try {
            PaginatedMenu menu = worldMenu;
            menu.getPage(menuPage);
            JsonObject obj = new Gson().fromJson(Database.getData(player.getUniqueId()), JsonObject.class).get("world_data").getAsJsonObject()
                    .get("animals").getAsJsonObject();
            for (int i = 1; i < obj.size() + 1; ++i) {
                AnimalType Enum = Utils.switchToAnimal(i);
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
        } catch (Exception e) {Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");}
    }
}