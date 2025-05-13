package me.webhead1104.township.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.township.Township;
import me.webhead1104.township.data.serializers.InstantSerializer;
import me.webhead1104.township.managers.UserManager;
import org.bukkit.Bukkit;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ConfigSerializable
@AllArgsConstructor
@NoArgsConstructor
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
        try {
            ConfigurationNode node = GsonConfigurationLoader.builder()
                    .defaultOptions(opts ->
                            opts.shouldCopyDefaults(true).serializers(builder -> builder.register(t -> t == Instant.class, new InstantSerializer())))
                    .buildAndLoadString(json);
            ConfigurationTransformation configurationTransformation = ConfigurationTransformation.versionedBuilder()
                    .addVersion(UserManager.LATEST_VERSION, ConfigurationTransformation.builder().build())
                    .build();
            configurationTransformation.apply(node);
            return node.get(User.class);
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst updating a user! Please report the following stacktrace to Webhead1104:", e);
        }
        throw new IllegalStateException("An error occurred whilst deserializing a user! Please report this to Webhead1104!");
    }

    @Override
    public String toString() {
        return Township.GSON.toJson(this);
    }
}