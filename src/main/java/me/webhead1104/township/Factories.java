package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.flame.menus.menu.Menu;
import me.flame.menus.modifiers.Modifier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.data.enums.ItemType;
import me.webhead1104.township.utils.MenuItems;
import me.webhead1104.township.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

public class Factories {

    public Factories() {}
    //"12-14 completed, 27 being worked on,36-44 rec"
    public static void factory(Player player, FactoryType type) {
        try {
            Factories factory = new Factories(player.getUniqueId());
            Menu menu = Menu.create(type.getFactoryName(), 5, EnumSet.allOf(Modifier.class));
            AtomicInteger var0 = new AtomicInteger(36);
            type.getRecipes().forEach((n) -> {
                menu.setItem(var0.get(), n);
                var0.set(var0.get() + 1);
            });
            //todo runnable loading
            JsonObject json = factory.getObj().get("factories").getAsJsonObject().get(type.getID().toLowerCase()).getAsJsonObject();
            int var1 = 12;
            for (int i = 1; i < json.size() + 1; ++i) {
                String var = json.get("completed").getAsJsonObject().get(String.valueOf(i)).getAsString();
                if (!var.equals("none")) {
                    ItemType item = Utils.getItem(var);
                    menu.setItem(var1, MenuItems.completed);
                    Objects.requireNonNull(menu.getItem(var1)).setItemStack(new ItemStack(item.getItemMaterial()));
                    Objects.requireNonNull(menu.getItem(var1)).editor().setName(item.getItemName());
                } else break;
            }
            menu.setItem(27, MenuItems.workingOn);
            Objects.requireNonNull(menu.getItem(27)).setItemStack(new ItemStack(Utils.getItem(json.get("working_on").getAsString()).getItemMaterial()));
            Objects.requireNonNull(menu.getItem(27)).editor().setName(Utils.getItem(json.get("working_on").getAsString()).getItemName());
            menu.setItem(36, MenuItems.backButton);
            menu.open(player);
        } catch (Exception e) {
            Township.INSTANCE.getLogger().log(Level.SEVERE, "ERROR " + e + " " + Arrays.toString(e.getStackTrace()));
        }
    }

    private UUID uuid;
    private JsonObject obj;

    public Factories(UUID uuid) {
        this.uuid = uuid;
        obj = new Gson().fromJson(Database.getData(uuid), JsonObject.class);
    }
    public JsonObject getObj() {return obj;}

    public Factories getFactories(UUID uuid) {
        return new Factories(uuid);
    }

    public UUID getUUID() {
        return uuid;
    }

    public ItemType getProduct(FactoryType type, int slot) {
        if (slot >= 1 && slot <= 3) {
            return Utils.getItem(obj.get("factories").getAsJsonObject()
                    .get(type.getID().toLowerCase()).getAsJsonObject().get(String.valueOf(slot)).getAsString());
        }else return ItemType.AIR;
    }

    public ItemType getWaiting(FactoryType type, int slot) {
        if (slot >= 1 && slot <= 3) {
            return Utils.getItem(obj.get("factories").getAsJsonObject()
                    .get(type.getID().toLowerCase()).getAsJsonObject().get(String.valueOf(slot)).getAsString());
        }else return ItemType.AIR;
    }

    public ItemType getWorkingOn(FactoryType type) {
        return Utils.getItem(obj.get("factories").getAsJsonObject().get(type.getID().toLowerCase())
                .getAsJsonObject().get("working_on").getAsString());
    }

    public boolean getUnlocked(FactoryType type) {
        return obj.get("world_data").getAsJsonObject().get("factories")
                .getAsJsonObject().get(type.getID().toLowerCase()).getAsJsonObject().get("unlocked").getAsBoolean();
    }

    public int getPage(FactoryType type) {
        return obj.get("world_data").getAsJsonObject().get("factories")
                .getAsJsonObject().get(type.getID().toLowerCase()).getAsJsonObject().get("page").getAsInt();
    }

    public int getSlot(FactoryType type) {
        return obj.get("world_data").getAsJsonObject().get("factories")
                .getAsJsonObject().get(type.getID().toLowerCase()).getAsJsonObject().get("slot").getAsInt();
    }
}