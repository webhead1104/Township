package me.webhead1104.township.data.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.Setter;
import me.webhead1104.township.data.datafixer.TownshipCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
public class User {
    public static final @NotNull Codec<User> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            TownshipCodecs.UUID.fieldOf("uuid").forGetter(User::getUuid),
            Codec.STRING.fieldOf("townName").forGetter(User::getTownName),
            PlayerLevel.CODEC.fieldOf("level").forGetter(User::getLevel),
            Codec.LONG.fieldOf("population").forGetter(User::getPopulation),
            Codec.LONG.fieldOf("coins").forGetter(User::getCoins),
            Codec.LONG.fieldOf("cash").forGetter(User::getCash),
            Codec.INT.fieldOf("section").forGetter(User::getSection),
            Barn.CODEC.fieldOf("barn").forGetter(User::getBarn),
            Animals.CODEC.fieldOf("animals").forGetter(User::getAnimals),
            Factories.CODEC.fieldOf("factories").forGetter(User::getFactories),
            World.CODEC.fieldOf("world").forGetter(User::getWorld),
            Trains.CODEC.fieldOf("trains").forGetter(User::getTrains)
    ).apply(instance, User::new));
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

    public User(UUID uuid, String townName, PlayerLevel level, long population, long coins, long cash, int section,
                Barn barn, Animals animals, Factories factories, World world, Trains trains) {
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
    }

    public static User fromJson(String json) {
        JsonOps jsonOps = JsonOps.INSTANCE;
        return User.CODEC.decode(jsonOps, JsonParser.parseString(json))
                .resultOrPartial(System.err::println)
                .map(Pair::getFirst)
                .orElse(null);
    }

    @Override
    public String toString() {
        JsonOps jsonOps = JsonOps.INSTANCE;
        return User.CODEC.encodeStart(jsonOps, this)
                .resultOrPartial(error -> System.err.println("Error serializing: " + error))
                .map(JsonElement::toString)
                .orElse("{}");

    }
}