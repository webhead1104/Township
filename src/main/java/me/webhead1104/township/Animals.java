package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.flame.menus.menu.Menu;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.AnimalsEnum;
import me.webhead1104.township.data.enums.ItemsEnum;
import me.webhead1104.township.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.logging.Level;

public class Animals {

    Township plugin;
    public Animals(Township plugin) {
        this.plugin = plugin;
    }

    public void cowshed(Player player) {
        try {
            AnimalsEnum animal = AnimalsEnum.COWSHED;
            Menu menu = Menu.create(animal.getAnimalName(),5);
            JsonObject object = new Gson().fromJson(Database.getPlayerData(player, "animals"), JsonObject.class).get(animal.getID().toLowerCase()).getAsJsonObject();
            int n = object.size() + 1;
            int slot = 11;
            for (int i = 1; i < n; ++i) {
                JsonObject forJson = object.get(String.valueOf(i)).getAsJsonObject();
                if (forJson.get("product").getAsBoolean())
                    menu.setItem(slot + 9, animal.getProduct());
                menu.setItem(slot, animal.getAnimal());
                if (forJson.get("feed").getAsBoolean())
                    menu.setItem(slot, animal.getAnimal().editor().setLore(ChatColor.GOLD + "Time: 0").done());
                slot++;
            }
            menu.setItem(36, animal.getFeed().editor().setLore(ChatColor.GOLD + Database.getItem(player, animal.getFeedType().getID().toLowerCase())).done());
            menu.open(player);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void cowshedFeed(Player player) {
        try {
            JsonObject object = new Gson().fromJson(Database.getPlayerData(player, "animals"), JsonObject.class).get("cowshed").getAsJsonObject();
            int var0 = object.size() + 1;
            for (int i = 1; i < var0; ++i) {
                int cow_feed = Integer.parseInt(Database.getItem(player, "cow_feed"));
                if (cow_feed > 0) {
                    JsonObject obj = object.get(String.valueOf(i)).getAsJsonObject();
                    if (!obj.get("feed").getAsBoolean()) {
                        obj.addProperty("feed", true);
                        Database.setPlayerData(player, "animals", object.toString());
                        Database.setItem(player, "cow_feed", String.valueOf(cow_feed + 1));
                    }
                } else break;
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void chickenCoop(Player player) {
        try {
            AnimalsEnum animal = AnimalsEnum.CHICKEN_COOP;
            Menu menu = Menu.create(animal.getAnimalName(),5);
            JsonObject object = new Gson().fromJson(Database.getPlayerData(player, "animals"), JsonObject.class).get(animal.getID().toLowerCase()).getAsJsonObject();
            int n = object.size() + 1;
            int slot = 11;
            for (int i = 1; i < n; ++i) {
                JsonObject forJson = object.get(String.valueOf(i)).getAsJsonObject();
                if (forJson.get("product").getAsBoolean())
                    menu.setItem(slot + 9, animal.getProduct());
                menu.setItem(slot, animal.getAnimal());
                if (forJson.get("feed").getAsBoolean())
                    menu.setItem(slot, animal.getAnimal().editor().setLore(ChatColor.GOLD + "Time: 0").done());
                slot++;
            }
            menu.setItem(36, animal.getFeed().editor().setLore(ChatColor.GOLD + Database.getItem(player, animal.getFeedType().getID().toLowerCase())).done());
            menu.open(player);
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void chickenCoopFeed(Player player) {
        try {
            JsonObject chickencoop = new Gson().fromJson(Database.getPlayerData(player, "animals"), JsonObject.class)
                    .get("chicken_coop").getAsJsonObject();
            plugin.getLogger().log(Level.INFO, "chickencoop = " + chickencoop);
            int var0 = chickencoop.size() + 1;
            for (int i = 1; i < var0; ++i) {
                JsonObject obj = chickencoop.get(String.valueOf(i)).getAsJsonObject();
                if (!obj.get("feed").getAsBoolean()) {
                    obj.addProperty("feed", true);
                    Database.setPlayerData(player, "animals", chickencoop.toString());
                    int chicken_feed = Integer.parseInt(Database.getItem(player, "chicken_feed"));
                    Database.setItem(player, "chicken_feed", String.valueOf(chicken_feed + 1));
                }
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void pickup(Player player, InventoryClickEvent event) {
        try {
            ItemStack item = event.getCurrentItem();
            assert item != null;
            if (event.getCurrentItem().hasItemMeta()) {
                ItemsEnum itemsEnum = Utils.getItem(item.getItemMeta().getDisplayName());
                AnimalsEnum animal = Utils.getAnimal(Utils.switchToID(itemsEnum.getID()));
                int var2 = event.getSlot() - 10;
                JsonObject obj = new Gson().fromJson(Database.getPlayerData(player, "animals"), JsonObject.class)
                        .get(animal.getID().toLowerCase()).getAsJsonObject().get(String.valueOf(var2)).getAsJsonObject();
                obj.addProperty("product", false);
                Database.setPlayerData(player,"animals",obj.toString());
                event.setCurrentItem(new ItemStack(Material.AIR));
                int var0 = Integer.parseInt(Database.getItem(player, itemsEnum.getID().toLowerCase()));
                Database.setItem(player, itemsEnum.getID().toLowerCase(), String.valueOf(var0 + 1));
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "ERROR " + Arrays.toString(e.getStackTrace()));
        }
    }
}