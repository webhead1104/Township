package me.webhead1104.township.data.objects;

import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.Township;
import org.bukkit.Bukkit;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class User {
    private UUID uuid;
    private String townName;
    private PlayerLevel level;
    private int population;
    private int maxPopulation;
    private long coins;
    private long cash;
    private int section;
    private Barn barn;
    private Animals animals;
    private Factories factories;
    private World world;
    private Trains trains;

    public User(UUID uuid, String townName, PlayerLevel level, int population, int maxPopulation, long coins, long cash, int section,
                Barn barn, Animals animals, Factories factories, World world, Trains trains) {
        this.uuid = uuid;
        this.townName = townName;
        this.level = level;
        this.population = population;
        this.maxPopulation = maxPopulation;
        this.coins = coins;
        this.cash = cash;
        this.section = section;
        this.barn = barn;
        this.animals = animals;
        this.factories = factories;
        this.world = world;
        this.trains = trains;
    }

    public User(UUID uuid) {
        long start = System.currentTimeMillis();
        this.uuid = uuid;
        this.townName = Bukkit.getPlayer(uuid) == null ? "none" : String.format("%s's Town", Objects.requireNonNull(Bukkit.getPlayer(uuid)).getName());
        this.level = new PlayerLevel(1, 0, uuid);
        this.population = 60;
        this.maxPopulation = 60;
        this.coins = 0;
        this.cash = 20;
        this.section = 27;
        this.barn = new Barn();
        this.animals = new Animals();
        this.factories = new Factories();
        this.world = new World();
        this.trains = new Trains();
        Township.logger.info("Finished creating a user in {} mills!", System.currentTimeMillis() - start);
    }

    public static User fromJson(String json) {
        return Township.GSON.fromJson(json, User.class);
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}