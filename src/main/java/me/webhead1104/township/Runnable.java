package me.webhead1104.township;

import com.google.gson.JsonObject;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.enums.AnimalType;
import me.webhead1104.township.data.enums.FactoryType;
import me.webhead1104.township.utils.Utils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class Runnable {

    public static Map<UUID,String> runnables = new HashMap<>();
    private int animal;
    private AnimalType animalType;
    private FactoryType factoryType;
    private final int time;
    private final UUID runnableUUID;

    public Runnable(JsonObject obj) {
        if (obj.get("one").getAsInt()!=0) {
            animal = obj.get("one").getAsInt();
            animalType = Utils.getAnimal(obj.get("type").getAsString());
        } else factoryType = Utils.getFactory(obj.get("type").getAsString());
        time = obj.get("time").getAsInt();
        runnableUUID = UUID.fromString(obj.get("uuid").getAsString());
    }



    public static void addRunable(int time, UUID playerUUID, AnimalType type, int animal, int runnableID) {
        UUID var1 = UUID.randomUUID();
        String var =
                STR."""
                {
                  "one": \{animal},
                  "time": \{time},
                  "type": "\{type.getID()}",
                  "uuid": "\{var1}",
                  "runnable_id": \{runnableID}
                }
                """;
        runnables.put(var1,var);
        JsonObject obj = Database.getData(playerUUID).get("animals").getAsJsonObject().get(type.getID())
                .getAsJsonObject().get(String.valueOf(animal)).getAsJsonObject();
        obj.addProperty("runnable_uuid", String.valueOf(var1));
        Database.setData(playerUUID,obj.toString());
    }

    public static void addRunable(int time, UUID playerUUID, FactoryType type, int runnableID) {
        UUID var1 = UUID.randomUUID();
        String var =
                STR."""
                {
                  "one": 0,
                  "time": \{time},
                  "type": "\{type.getID()}",
                  "uuid": "\{var1}",
                  "runnable_id": \{runnableID}
                }
                """;
        runnables.put(var1,var);
        JsonObject obj = Database.getData(playerUUID).get("factories").getAsJsonObject().get(type.getID())
                .getAsJsonObject();
        obj.addProperty("runnable_uuid", String.valueOf(var1));
        Database.setData(playerUUID,obj.toString());
    }

    public static Runnable getRunnable(JsonObject obj) {return new Runnable(obj);}
    public int getAnimal() {return animal;}
    public AnimalType getAnimalType() {return animalType;}
    public FactoryType getFactoryType() {return factoryType;}
    public int getTime() {return time;}
    public UUID getRunnableUUID() {return runnableUUID;}

    public abstract void run();
}
