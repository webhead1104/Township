package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.flame.menus.items.MenuItem;
import me.flame.menus.menu.Menu;
import me.flame.menus.menu.PaginatedMenu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.ItemsEnum;
import me.webhead1104.township.utils.Items;
import me.webhead1104.township.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.logging.Level;

public class Factories {

    Township plugin;
    public PaginatedMenu feedmill = PaginatedMenu.create(ChatColor.AQUA + "Feed Mill", 5, 1, EnumSet.allOf(Modifier.class));
    public PaginatedMenu dairy = PaginatedMenu.create(ChatColor.AQUA + "Dairy Factory", 5, 1, EnumSet.allOf(Modifier.class));
    public PaginatedMenu sugar = PaginatedMenu.create(ChatColor.AQUA + "Sugar Factory", 5, 1, EnumSet.allOf(Modifier.class));
    public Factories(Township plugin) {this.plugin = plugin;}
    //"12-14 completed, 27 being worked on,36-44 rec"
    public void bakery(Player player) {
        try {
            Menu menu = Menu.create(ChatColor.BLUE + "Bakery", 5, EnumSet.allOf(Modifier.class));
            JsonObject bakery = new Gson().fromJson(Database.getPlayerData(player, "factories"), JsonObject.class)
                    .get("bakery").getAsJsonObject();
            menu.setItem(39, Items.breadRecipe);
            menu.setItem(40, Items.cookieRecipe);
            menu.setItem(41, Items.bagelRecipe);
            menu.setItem(27, Items.workingOn);
            if (!bakery.get("working_on").getAsString().equals("none")) {
                ItemsEnum working_on = Utils.getItem(bakery.get("working_on").getAsString());
                Objects.requireNonNull(menu.getItem(27)).setItemStack(new ItemStack(working_on.getItemMaterial()));
                Objects.requireNonNull(menu.getItem(27)).editor().setName(working_on.getItemName())
                        .setLore(ChatColor.GOLD + "Time: 0").done();
            }
            plugin.getLogger().log(Level.INFO,"working_on done id = "+Utils.getItem(bakery.get("working_on").getAsString()).getID());
            JsonObject completed = bakery.get("completed").getAsJsonObject();
            menu.setItem(12,Items.completed);
            if (!completed.get("1").getAsString().equals("none")) {
                String var = completed.get("1").getAsString();
                MenuItem menuItem = menu.getItem(12);
                assert menuItem != null;
                menuItem.setItemStack(new ItemStack(Utils.getItem(var).getItemMaterial()));
                menuItem.editor().setName(Utils.getItem(var).getItemName()).done();
                plugin.getLogger().log(Level.INFO,"1 called id = "+Utils.getItem(var));
            }
            plugin.getLogger().log(Level.INFO,"one done id = "+Utils.getItem(completed.get("1").getAsString()).getID());
            menu.setItem(13,Items.completed);
            if (!completed.get("2").getAsString().equals("none")) {
                String var = completed.get("2").getAsString();
                MenuItem menuItem = menu.getItem(13);
                assert menuItem != null;
                menuItem.setItemStack(new ItemStack(Utils.getItem(var).getItemMaterial()));
                menuItem.editor().setName(Utils.getItem(var).getItemName()).done();
                plugin.getLogger().log(Level.INFO,"2 called id = "+Utils.getItem(var));
            }
            plugin.getLogger().log(Level.INFO,"two done id = "+Utils.getItem(completed.get("2").getAsString()).getID());
            menu.setItem(14,Items.completed);
            if (!completed.get("3").getAsString().equals("none")) {
                String var = completed.get("3").getAsString();
                MenuItem menuItem = menu.getItem(14);
                assert menuItem != null;
                menuItem.setItemStack(new ItemStack(Utils.getItem(var).getItemMaterial()));
                menuItem.editor().setName(Utils.getItem(var).getItemName()).done();
                plugin.getLogger().log(Level.INFO,"3 called id = "+Utils.getItem(var));
            }
            plugin.getLogger().log(Level.INFO,"3 done id = "+Utils.getItem(completed.get("3").getAsString()).getID());
            plugin.getLogger().log(Level.INFO,"bakery = "+bakery+" completed = "+completed);
            menu.setItem(36, Items.backButton);
            plugin.getLogger().log(Level.INFO, "after back button");
            menu.open(player);
            plugin.getLogger().log(Level.INFO, "after open");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void feedmill(Player player) {
        feedmill.setItem(36, Items.backButton);
        feedmill.open(player);
    }

    public void dairy(Player player) {
        dairy.setItem(36, Items.backButton);
        dairy.open(player);
    }

    public void sugar(Player player) {
        sugar.setItem(36, Items.backButton);
        sugar.open(player);
    }
}