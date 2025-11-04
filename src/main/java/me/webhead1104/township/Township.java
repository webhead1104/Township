package me.webhead1104.township;

import com.google.common.base.Stopwatch;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewFrame;
import me.webhead1104.township.annotations.DependsOn;
import me.webhead1104.township.commands.TownshipCommandBrigadier;
import me.webhead1104.township.dataLoaders.DataLoader;
import me.webhead1104.township.database.LoaderManager;
import me.webhead1104.township.listeners.JoinListener;
import me.webhead1104.township.listeners.LeaveListener;
import me.webhead1104.township.managers.InventoryManager;
import me.webhead1104.township.managers.UserManager;
import me.webhead1104.township.serializers.TownshipSerializer;
import me.webhead1104.township.utils.ClassGraphUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class Township extends JavaPlugin {
    public static final Key noneKey = key("none");
    private static final File PLUGIN_DIR = new File("plugins", "Township");
    private static final File CONFIG_FILE = new File(PLUGIN_DIR, "config.yml");
    @Getter
    private static final Map<Class<? extends DataLoader>, DataLoader> dataLoaders = new HashMap<>();
    public static Logger logger;
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final GsonConfigurationLoader.Builder GSON_CONFIGURATION_LOADER = GsonConfigurationLoader.builder().defaultOptions(opts -> opts.shouldCopyDefaults(true).serializers(builder -> {
        for (TownshipSerializer<?> townshipSerializer : ClassGraphUtils.getExtendedClasses(TownshipSerializer.class, "me.webhead1104.township.serializers")) {
            Class targetClass = townshipSerializer.getTargetClass();
            builder.register(targetClass, townshipSerializer);
        }
    }));
    @Getter
    private static LoaderManager loaderManager;
    @Getter
    private static InventoryManager inventoryManager;
    @Getter
    private static UserManager userManager;
    @Getter
    private static ViewFrame viewFrame;
    @Getter
    private static Config townshipConfig;
    @Getter
    private static Township instance;

    public static Key key(@KeyPattern.Value String value) {
        return Key.key("township", value);
    }

    public static <T extends DataLoader> T getDataLoader(Class<T> dataLoaderClass) {
        T loader = dataLoaderClass.cast(dataLoaders.get(dataLoaderClass));
        if (loader == null) {
            throw new IllegalStateException("Data loader not found: " + dataLoaderClass.getName());
        }
        return loader;
    }

    public static void loadConfig() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder()
                    .file(CONFIG_FILE)
                    .commentsEnabled(true)
                    .nodeStyle(NodeStyle.BLOCK)
                    .headerMode(HeaderMode.PRESERVE)
                    .build();

            if (!CONFIG_FILE.exists()) {
                townshipConfig = new Config();
                saveTownshipConfig();
                logger.info("Created new config file");
            }

            ConfigurationNode node = configLoader.load();

            if (node.node("version").getInt() == 0) {
                logger.info("Migrating your old config file!");
                ConfigurationNode oldConfigMysql = node.node("mysql");

                Config.MysqlConfig mysqlConfig = new Config.MysqlConfig();
                mysqlConfig.setHost(oldConfigMysql.node("host").getString());
                mysqlConfig.setPort(oldConfigMysql.node("port").getInt());
                mysqlConfig.setDatabase(oldConfigMysql.node("database").getString());
                mysqlConfig.setUsername(oldConfigMysql.node("username").getString());
                mysqlConfig.setPassword(oldConfigMysql.node("password").getString());
                mysqlConfig.setUseSsl(oldConfigMysql.node("use_ssl").getBoolean());
                townshipConfig = node.get(Config.class);

                if (townshipConfig != null) {
                    townshipConfig.getDatabase().setMysqlConfig(mysqlConfig);
                }

                saveTownshipConfig();
                logger.info("Config migration complete");
            } else {
                townshipConfig = node.get(Config.class);
            }

            loaderManager = new LoaderManager();
            logger.info("Loaded config in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            logger.error("Could not load config!", e);
        }
    }

    public static void saveTownshipConfig() {
        try {
            YamlConfigurationLoader configLoader = YamlConfigurationLoader.builder().file(CONFIG_FILE)
                    .nodeStyle(NodeStyle.BLOCK).headerMode(HeaderMode.PRESERVE).build();
            configLoader.save(configLoader.createNode().set(townshipConfig));
        } catch (Exception e) {
            logger.error("An error occurred whilest saving the config!", e);
        }
    }

    @Override
    public void onLoad() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> TownshipCommandBrigadier.register(event.registrar())));
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        logger = getSLF4JLogger();
        registerListeners();
        loadConfig();
        viewFrame = ViewFrame.create(this);
        inventoryManager = new InventoryManager();
        userManager = new UserManager();
        userManager.loadDataVersions();
        loadDataLoaders();
        registerViews();
        logger.info("Township initialized in {} mills!", System.currentTimeMillis() - start);
    }

    @Override
    public void onDisable() {
        logger.info("Township shutting down saving users");
        userManager.getUsers().forEach((key, user) -> {
            user.save();
            logger.info("saved {}", user.getUuid());
        });
        logger.info("Township has shut down!");
    }

    public void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new LeaveListener(), this);
    }

    private void loadDataLoaders() {
        List<DataLoader> dataLoaders = ClassGraphUtils.getImplementedClasses(DataLoader.class, "me.webhead1104.township");

        Set<Class<? extends DataLoader>> loaded = new HashSet<>();
        Set<Class<? extends DataLoader>> loading = new HashSet<>();

        for (DataLoader dataLoader : dataLoaders) {
            try {
                loadWithDependencies(dataLoader, dataLoaders, loaded, loading);
            } catch (Exception e) {
                logger.error("Failed to load data loader: {}", dataLoader.getClass().getName(), e);
            }
        }
    }

    private void loadWithDependencies(
            DataLoader dataLoader,
            List<DataLoader> allDataLoaders,
            Set<Class<? extends DataLoader>> loaded,
            Set<Class<? extends DataLoader>> loading) {

        Class<? extends DataLoader> loaderClass = dataLoader.getClass();
        if (loaded.contains(loaderClass)) {
            return;
        }
        if (loading.contains(loaderClass)) {
            throw new IllegalStateException("Circular dependency detected for: " + loaderClass.getName());
        }

        loading.add(loaderClass);

        DependsOn dependsOn = loaderClass.getAnnotation(DependsOn.class);
        if (dependsOn != null) {
            for (Class<? extends DataLoader> dependencyClass : dependsOn.value()) {
                DataLoader dependency = allDataLoaders.stream()
                        .filter(loader -> dependencyClass.isAssignableFrom(loader.getClass()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException(
                                "Dependency not found: " + dependencyClass.getName() +
                                        " required by " + loaderClass.getName()));

                loadWithDependencies(dependency, allDataLoaders, loaded, loading);
            }
        }

        logger.debug("Loading data loader: {}", loaderClass.getName());
        dataLoader.load();

        loading.remove(loaderClass);
        loaded.add(loaderClass);

        dataLoaders.put(loaderClass, dataLoader);
    }

    private void registerViews() {
        for (View view : ClassGraphUtils.getExtendedClasses(View.class, "me.webhead1104.township.features")) {
            viewFrame.with(view);
        }

        viewFrame.register();
    }
}