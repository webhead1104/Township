package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.flame.menus.menu.Menu;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class Animals {

    public Animals() {
    }

    public Menu animal(Player player, AnimalType animal) {
        Menu menu = Menu.create(animal.getAnimalName(), 5);
        try {
            JsonObject obj = new Gson().fromJson(Database.getData(player.getUniqueId()), JsonObject.class).get(animal.getID().toLowerCase()).getAsJsonObject();
            int n = obj.size() + 1;
            int slot = 11;
            for (int i = 1; i < n; ++i) {
                JsonObject forJson = obj.get(String.valueOf(i)).getAsJsonObject();
                if (forJson.get("product").getAsBoolean())
                    menu.setItem(slot + 9, animal.getProduct());
                menu.setItem(slot, animal.getAnimal());
                if (forJson.get("feed").getAsBoolean())
                    menu.setItem(slot, animal.getAnimal().editor().setLore(STR."\{ChatColor.GOLD}Time: 0").done());
                slot++;
            }
            menu.setItem(36, animal.getFeed().editor().setLore(ChatColor.GOLD + Database.getItem(player.getUniqueId(), animal.getFeedType().getID().toLowerCase())).done());

        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");
        }
        return menu;
    }

    public void feed(Player player, AnimalType animal) {
        try {
            JsonObject object = new Gson().fromJson(Database.getData(player.getUniqueId()), JsonObject.class).get(animal.getID().toLowerCase()).getAsJsonObject();
            int var0 = object.size() + 1;
            for (int i = 1; i < var0; ++i) {
                int feed = Integer.parseInt(Database.getItem(player.getUniqueId(), animal.getFeedType().getID().toLowerCase()));
                if (feed > 0) {
                    JsonObject obj = object.get(String.valueOf(i)).getAsJsonObject();
                    if (!obj.get("feed").getAsBoolean()) {
                        obj.addProperty("feed", true);
                        Database.setData(player.getUniqueId(), object.toString());
                        Database.setItem(player.getUniqueId(), animal.getID().toLowerCase(), String.valueOf((feed + 1)));


                        int finalI = i;
                        int id = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Township.INSTANCE, new org.bukkit.scheduler.BukkitRunnable() {
                            int var = 0;
                            @Override
                            public void run() {
                                if (var != 10) {
                                    var--;
                                    Menu menu = animal(player,animal);
                                    int slot = finalI+10;
                                    menu.getItem(slot).editor().setName("");
                                }else {
                                    JsonObject obj = new Gson().fromJson(Database.getData(player.getUniqueId()), JsonObject.class).get("animals").getAsJsonObject()
                                            .get(animal.getID().toLowerCase()).getAsJsonObject().get(String.valueOf(finalI)).getAsJsonObject();
                                    obj.addProperty("feed", false);
                                    obj.addProperty("product", true);
                                    obj.addProperty("runnable_uuid", "none");
                                    Database.setData(player.getUniqueId(),obj.toString());
                                    cancel();
                                }
                            }
                        }, 0L, 100L); // 20 ticks = 1 second
                         Runnable.addRunable(0,player.getUniqueId(),animal,i,id);




                    }
                } else break;
            }
        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");
        }
    }

    public void pickup(Player player, InventoryClickEvent event) {
        try {
            ItemStack item = event.getCurrentItem();
            assert item != null;
            if (event.getCurrentItem().hasItemMeta()) {
                ItemType itemType = Utils.getItem(item.getItemMeta().getDisplayName());
                AnimalType animal = Utils.getAnimal(Utils.switchToID(itemType.getID()));
                int var2 = event.getSlot() - 10;
                JsonObject obj = new Gson().fromJson(Database.getData(player.getUniqueId()), JsonObject.class).get("animals").getAsJsonObject()
                        .get(animal.getID().toLowerCase()).getAsJsonObject().get(String.valueOf(var2)).getAsJsonObject();
                obj.addProperty("product", false);
                Database.setData(player.getUniqueId(), obj.toString());
                event.setCurrentItem(new ItemStack(Material.AIR));
                int var0 = Integer.parseInt(Database.getItem(player.getUniqueId(), itemType.getID().toLowerCase()));
                Database.setItem(player.getUniqueId(), itemType.getID().toLowerCase(), String.valueOf(var0 + 1));
            }
        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, STR."ERROR \{e} \{Arrays.toString(e.getStackTrace())}");
        }
    }

    private static UUID uuid;
    private static JsonObject obj;

    public Animals(UUID uuid) {
        Animals.uuid = uuid;
        obj = new Gson().fromJson(Database.getData(uuid), JsonObject.class);
    }


    public static Animals getAnimals(UUID uuid) {
        return new Animals(uuid);
    }
    public static UUID getUuid() {return uuid;}
    public static boolean getFeed(AnimalType animalType,int animal) {
        return obj.get("animals").getAsJsonObject().get(animalType.getID().toLowerCase())
                .getAsJsonObject().get(String.valueOf(animal)).getAsBoolean();
    }
    public static boolean getProduct(AnimalType animalType, int animal) {
        return obj.get("animals").getAsJsonObject().get(animalType.getID().toLowerCase())
                .getAsJsonObject().get(String.valueOf(animal)).getAsBoolean();
    }
    public static boolean getUnlocked(AnimalType animalType) {
        return obj.get("world_data").getAsJsonObject().get("animals").getAsJsonObject()
                .get(animalType.getID().toLowerCase()).getAsJsonObject().get("unlocked").getAsBoolean();
    }
    public static int getAnimalPage(AnimalType animalType) {
        return obj.get("world_data").getAsJsonObject().get("animals").getAsJsonObject()
                .get(animalType.getID().toLowerCase()).getAsJsonObject().get("page").getAsInt();
    }
    public static int getAnimalSlot(AnimalType animalType) {
        return obj.get("world_data").getAsJsonObject().get("animals").getAsJsonObject()
                .get(animalType.getID().toLowerCase()).getAsJsonObject().get("slot").getAsInt();
    }
}