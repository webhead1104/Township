package me.webhead1104.towncraft.data.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.webhead1104.towncraft.Towncraft;
import me.webhead1104.towncraft.TowncraftPlayer;
import me.webhead1104.towncraft.dataLoaders.LevelDataLoader;
import me.webhead1104.towncraft.features.world.build.BuildingType;
import me.webhead1104.towncraft.utils.Msg;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@ConfigSerializable
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public static final int LATEST_VERSION = 13;
    private int version = LATEST_VERSION;
    private UUID uuid;
    private String townName;
    private int level;
    private int xp;
    private int population;
    private int maxPopulation;
    private int coins;
    private int cash;
    private int section;
    private Barn barn;
    private Animals animals;
    private Factories factories;
    private World world;
    private Trains trains;
    private PurchasedBuildings purchasedBuildings;
    private int expansionsPurchased;

    public User(UUID uuid) {
        long start = System.currentTimeMillis();
        this.uuid = uuid;
        this.townName = Towncraft.getPlayer(uuid) == null ? "none" : String.format("%s's Town", Objects.requireNonNull(Towncraft.getPlayer(uuid)).getName());
        this.level = 1;
        this.xp = 0;
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
        this.purchasedBuildings = new PurchasedBuildings();
        this.expansionsPurchased = 0;
        Towncraft.getLogger().info("Finished creating a user in {} mills!", System.currentTimeMillis() - start);
    }

    public static User fromJson(String json) {
        try {
            ConfigurationNode node = Towncraft.GSON_CONFIGURATION_LOADER.buildAndLoadString(json);
            int version = node.node("version").getInt();
            //todo
//            Towncraft.getUserManager().getVersionedBuilder().build().apply(node);
            User user = node.get(User.class);
            if (user == null) {
                throw new IllegalStateException("An error occurred whilst deserializing a user! Please report this to Webhead1104!\n USER IS NULL!!!");
            }
            user.recalculatePopulation();
            user.save();
            if (version != user.getVersion()) {
                Towncraft.getLogger().info("Updated user from {} to {}", version, user.getVersion());
            }
            return user;
        } catch (Exception e) {
            Towncraft.getLogger().error("An error occurred whilst updating a user! Please report the following stacktrace to Webhead1104:", e);
        }
        throw new IllegalStateException("An error occurred whilst deserializing a user! Please report this to Webhead1104!");
    }

    @Override
    public String toString() {
        try {
            StringWriter stringWriter = new StringWriter();
            ConfigurationNode node = Towncraft.GSON_CONFIGURATION_LOADER.build().createNode();
            node.set(this);
            Towncraft.GSON_CONFIGURATION_LOADER.sink(() -> new BufferedWriter(stringWriter)).build().save(node);
            return stringWriter.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void recalculatePopulation() {
        AtomicInteger population = new AtomicInteger(60);
        AtomicInteger maxPopulation = new AtomicInteger(60);
        purchasedBuildings.getPurchasedBuildings().forEach((buildingType, list) ->
                list.stream().filter(PurchasedBuildings.PurchasedBuilding::isPlaced).forEach(value -> {
                    BuildingType.Building building = value.getBuilding();
                    population.addAndGet(building.getPopulationIncrease());
                    maxPopulation.addAndGet(building.getMaxPopulationIncrease());
                }));
        this.population = population.get();
        this.maxPopulation = maxPopulation.get();
    }

    public void addXp(int amountOfXp) {
        if (amountOfXp <= 0) return; // ignore non-positive xp changes here
        xp = xp + amountOfXp;
        //noinspection StatementWithEmptyBody
        while (canLevelUp()) {
        }
    }

    public boolean canLevelUp() {
        LevelDataLoader.Level nextLevel = Towncraft.getDataLoader(LevelDataLoader.class).get(level + 1);
        if (nextLevel == null) {
            return false;
        }
        int needed = nextLevel.getXpNeeded();
        if (xp >= needed) {
            // level up
            level = level + 1;
            // subtract the XP required so overflow carries to next level
            xp = xp - needed;

            // Apply rewards for reaching this level
            int coinsReward = nextLevel.getCoinsGiven();
            int cashReward = nextLevel.getCashGiven();
            if (coinsReward > 0) coins = coins + coinsReward;
            if (cashReward > 0) cash = cash + cashReward;

            // Notify player
            StringBuilder sb = new StringBuilder();
            sb.append("<green>You have leveled up to level ").append(level).append("!");
            if (coinsReward > 0 || cashReward > 0) {
                sb.append(" <gray>(Rewards: ");
                boolean added = false;
                if (coinsReward > 0) {
                    sb.append("<yellow>").append(coinsReward).append(" coins");
                    added = true;
                }
                if (cashReward > 0) {
                    sb.append(added ? "<gray>, " : "").append("<aqua>").append(cashReward).append(" cash");
                }
                sb.append("<gray>)");
            }
            TowncraftPlayer player = Towncraft.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(Msg.format(sb.toString()));
            }
            return true;
        }
        return false;
    }

    public void save() {
        //todo
//        try {
//            Towncraft.getLoaderManager().getUserLoader().saveUser(uuid, toString());
//        } catch (IOException e) {
//            Towncraft.logger.error("An error occurred whilst saving a user!", e);
//        }
    }
}