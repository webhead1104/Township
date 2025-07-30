package me.webhead1104.township;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.devnatan.inventoryframework.ViewFrame;
import me.webhead1104.township.commands.TownshipCommandBrigadier;
import me.webhead1104.township.data.Database;
import me.webhead1104.township.listeners.JoinListener;
import me.webhead1104.township.listeners.LeaveListener;
import me.webhead1104.township.managers.*;
import me.webhead1104.township.menus.*;
import me.webhead1104.township.serializers.InstantSerializer;
import me.webhead1104.township.serializers.TileSerializer;
import me.webhead1104.township.tiles.Tile;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.time.Instant;

@NoArgsConstructor
public class Township extends JavaPlugin {

    public static final GsonConfigurationLoader.Builder GSON_CONFIGURATION_LOADER = GsonConfigurationLoader.builder()
            .defaultOptions(opts -> opts.shouldCopyDefaults(true).serializers(builder -> {
                builder.register(t -> t == Instant.class, new InstantSerializer());
                builder.register(t -> {
                    if (t instanceof Class<?> clazz) {
                        return clazz == Tile.class || Tile.class.isAssignableFrom(clazz);
                    }
                    return false;
                }, new TileSerializer());
            }));

    public static Logger logger;
    @Getter
    private static Database database;
    @Getter
    private static WorldManager worldManager;
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
    private static ViewFrame viewFrame;
    @Getter
    private static Township instance;

    @Override
    public void onLoad() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> TownshipCommandBrigadier.register(event.registrar())));
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        registerListeners();
        saveDefaultConfig();
        logger = getSLF4JLogger();
        database = new Database(this);
        database.connect();
        database.createTownshipTable();
        viewFrame = ViewFrame.create(this);
        registerViews();
        viewFrame.register();
        worldManager = new WorldManager();
        inventoryManager = new InventoryManager();
        userManager = new UserManager();
        barnManager = new BarnManager();
        barnManager.loadPages();
        barnManager.loadUpgrades();
        trainManager = new TrainManager();
        levelManager = new LevelManager();
        levelManager.loadLevels();
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
//        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(), this);
//        this.getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);
    }

    private void registerViews() {
        viewFrame.with(
                new AnimalMenu(), new FactoryMenu(), new WorldMenu(), new ExpansionMenu(),
                new ConfirmCloseMenu(), new PlotMenu()
        );
    }
}