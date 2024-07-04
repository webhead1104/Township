package me.webhead1104.township.data.objects;

import com.google.gson.JsonObject;
import lombok.Getter;
import me.webhead1104.township.Animals;
import me.webhead1104.township.Factories;
import me.webhead1104.township.data.Database;
import java.util.UUID;

@Getter
public class User {
    private final JsonObject obj;
    private final UUID uuid;
    private final String townName;
    private final int level;
    private final int population;
    private final int coins;
    private final int cash;
    private final Items items;
    private final Animals animals;
    private final Factories factories;

    public User(UUID uuid) {
        this.uuid = uuid;
        obj = Database.getData(uuid);
        this.townName = obj.get("townName").getAsString();
        this.level = obj.get("level").getAsInt();
        this.population = obj.get("population").getAsInt();
        this.coins = obj.get("coins").getAsInt();
        this.cash = obj.get("cash").getAsInt();
        this.items = Items.getItems(uuid);
        this.animals = Animals.getAnimals(uuid);
        this.factories = new Factories().getFactories(uuid);
    }
}