package me.webhead1104.township;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.velix.imperat.BukkitImperat;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.Imperat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.webhead1104.township.commands.TownshipCommand;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.data.adapters.InstantAdapter;
import me.webhead1104.township.data.objects.User;
import me.webhead1104.township.data.serializers.InstantSerializer;
import me.webhead1104.township.listeners.InventoryClickListener;
import me.webhead1104.township.listeners.InventoryCloseListener;
import me.webhead1104.township.listeners.JoinListener;
import me.webhead1104.township.listeners.LeaveListener;
import me.webhead1104.township.managers.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.time.Instant;

@NoArgsConstructor
public class Township extends JavaPlugin {

    public static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Instant.class, new InstantAdapter())
            .create();
    public static final GsonConfigurationLoader.Builder GSON_CONFIGURATION_LOADER = GsonConfigurationLoader.builder()
            .defaultOptions(opts -> opts.shouldCopyDefaults(true).serializers(builder -> builder.register(t -> t == Instant.class, new InstantSerializer())));

    public static Logger logger;
    @Getter
    private static Database database;
    @Getter
    private static WorldManager worldManager;
    @Getter
    private static ExpansionManager expansionManager;
    @Getter
    private static AnimalsManager animalsManager;
    @Getter
    private static FactoriesManager factoriesManager;
    @Getter
    private static PlotManager plotManager;
    @Getter
    private static InventoryManager inventoryManager;
    @Getter
    private static UserManager userManager;
    @Getter
    private static BarnManager barnManager;
    @Getter
    private static TrainManager trainManager;
    @Getter
    private static LevelManager levelManager;
    @Getter
    private static Township instance;
    @Getter
    private static Imperat<BukkitSource> imperat;

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        imperat = BukkitImperat.builder(this).applyBrigadier(true).build();
        imperat.registerCommand(new TownshipCommand());
        registerListeners();
        saveDefaultConfig();
        logger = getSLF4JLogger();
        database = new Database(this);
        database.connect();
        database.createTownshipTable();
        worldManager = new WorldManager();
        expansionManager = new ExpansionManager();
        animalsManager = new AnimalsManager();
        factoriesManager = new FactoriesManager();
        plotManager = new PlotManager();
        inventoryManager = new InventoryManager();
        userManager = new UserManager();
        barnManager = new BarnManager();
        barnManager.loadPages();
        barnManager.loadUpgrades();
        trainManager = new TrainManager();
        levelManager = new LevelManager();
        levelManager.loadLevels();
        User.fromJson("""
                {"hello":1}
                """);
        logger.info("Township initialized in {} mills!", System.currentTimeMillis() - start);
    }

    @Override
    public void onDisable() {
        logger.info("Township shutting down saving users");
        userManager.getUsers().forEach((key, user) -> {
            database.setData(user);
            logger.info("saved {}", user.getUuid());
        });
        database.disconnect();
        logger.info("Township has shut down!");
    }

    public void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
    }
}
