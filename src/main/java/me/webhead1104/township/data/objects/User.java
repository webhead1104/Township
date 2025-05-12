package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;

import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID uuid;
    private String townName;
    private PlayerLevel level;
    private long population;
    private long coins;
    private long cash;
    private int section;
    private Barn barn;
    private Animals animals;
    private Factories factories;
    private World world;
    private Trains trains;
    private PurchasedBuildings purchasedBuildings;

    public User(UUID uuid, String townName, PlayerLevel level, long population, long coins, long cash, int section,
                Barn barn, Animals animals, Factories factories, World world, Trains trains, PurchasedBuildings purchasedBuildings) {
        this.uuid = uuid;
        this.townName = townName;
        this.level = level;
        this.population = population;
        this.coins = coins;
        this.cash = cash;
        this.section = section;
        this.barn = barn;
        this.animals = animals;
        this.factories = factories;
        this.world = world;
        this.trains = trains;
        this.purchasedBuildings = purchasedBuildings;
    }

    public static User fromJson(String json) {
        return Township.GSON.fromJson(json, User.class);
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}