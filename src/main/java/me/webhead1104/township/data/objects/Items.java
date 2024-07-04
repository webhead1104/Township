package me.webhead1104.township.data.objects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.ItemType;

import java.util.UUID;

public class Items {

    public Items() {
    }

    private static UUID uuid;
    private static JsonObject obj;

    public Items(UUID uuid) {
        Items.uuid = uuid;
        obj = new Gson().fromJson(Database.getData(uuid), JsonObject.class);
    }

    public static Items getItems(UUID uuid) {
        return new Items(uuid);
    }

    public static int getItem(ItemType itemType) {
        return obj.get(itemType.getID().toLowerCase()).getAsInt();
    }
}