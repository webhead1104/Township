package me.webhead1104.towncraft;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewFrame;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.database.LoaderManager;
import me.webhead1104.towncraft.utils.ClassGraphUtils;
import net.kyori.adventure.util.Services;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TowncraftPlatformManager {
    @Getter
    private final static TowncraftPlatform platform = Services.service(TowncraftPlatform.class)
            .orElseThrow(() -> new IllegalStateException("No Towncraft found!"));
    private static final File OLD_PLUGIN_DIR = new File("plugins", "Township");
    private static final File CONFIG_FILE = new File(platform.getDataFolder(), "config.yml");
    @Getter
    private static final Map<Class<? extends DataLoader>, DataLoader> dataLoaders = new HashMap<>();
    @Getter
    private static ViewFrame viewFrame;
    @Getter
    private static UserManager userManager;
    @Getter
    private static LoaderManager loaderManager;
    @Getter
    private static Config config;

    public static <T extends DataLoader> T getDataLoader(Class<T> dataLoaderClass) {
        T loader = dataLoaderClass.cast(dataLoaders.get(dataLoaderClass));
        if (loader == null) {
            throw new IllegalStateException("Data loader not found: " + dataLoaderClass.getName());
        }
        return loader;
    }

    public static void init() {
//        ImperatDebugger.setEnabled(true);
//        System.setProperty("me.devnatan.inventoryframework.debug", "true");
        loadConfig();
        loadDataLoaders();

        viewFrame = new ViewFrame();
        registerViews();

        userManager = new UserManager();
        platform.init();
    }

    public static void shutdown() {
        viewFrame.unregister();
        platform.shutdown();
    }

    public static void saveTowncraftConfig() {
        try {
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder().file(CONFIG_FILE)
                    .nodeStyle(NodeStyle.BLOCK).headerMode(HeaderMode.PRESERVE).build();
            loader.save(loader.createNode().set(config));
        } catch (Exception e) {
            platform.getLogger().error("An error occurred whilest saving the config!", e);
        }
    }

    public static void loadConfig() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            if (OLD_PLUGIN_DIR.exists()) {
                if (!OLD_PLUGIN_DIR.renameTo(platform.getDataFolder())) {
                    platform.getLogger().error("Could not rename config file!");
                }
            }

            YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                    .file(CONFIG_FILE)
                    .commentsEnabled(true)
                    .nodeStyle(NodeStyle.BLOCK)
                    .headerMode(HeaderMode.PRESERVE)
                    .build();

            if (!CONFIG_FILE.exists()) {
                config = new Config();
                saveTowncraftConfig();
                platform.getLogger().info("Created new config file");
            }

            ConfigurationNode node = loader.load();

            if (node.node("version").getInt() == 0) {
                platform.getLogger().info("Migrating your old config file!");
                ConfigurationNode oldConfigMysql = node.node("mysql");

                Config.MysqlConfig mysqlConfig = new Config.MysqlConfig();
                mysqlConfig.setHost(oldConfigMysql.node("host").getString());
                mysqlConfig.setPort(oldConfigMysql.node("port").getInt());
                mysqlConfig.setDatabase(oldConfigMysql.node("database").getString());
                mysqlConfig.setUsername(oldConfigMysql.node("username").getString());
                mysqlConfig.setPassword(oldConfigMysql.node("password").getString());
                mysqlConfig.setUseSsl(oldConfigMysql.node("use_ssl").getBoolean());
                config = node.get(Config.class);

                if (config != null) {
                    config.getDatabase().setMysqlConfig(mysqlConfig);
                }

                saveTowncraftConfig();
                platform.getLogger().info("Config migration complete");
            } else {
                config = node.get(Config.class);
            }

            loaderManager = new LoaderManager();
            platform.getLogger().info("Loaded config in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            platform.getLogger().error("Could not load config!", e);
        }
    }

    private static void loadDataLoaders() {
        List<DataLoader> dataLoaders = ClassGraphUtils.getImplementedClasses(DataLoader.class, "me.webhead1104.towncraft");

        Set<Class<? extends DataLoader>> loaded = new HashSet<>();
        Set<Class<? extends DataLoader>> loading = new HashSet<>();

        for (DataLoader dataLoader : dataLoaders) {
            try {
                loadWithDependencies(dataLoader, dataLoaders, loaded, loading);
            } catch (Exception e) {
                Towncraft.getLogger().error("Failed to load data loader: {}", dataLoader.getClass().getName(), e);
            }
        }
    }

    private static void loadWithDependencies(
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

        Towncraft.getLogger().debug("Loading data loader: {}", loaderClass.getName());
        dataLoader.load();

        loading.remove(loaderClass);
        loaded.add(loaderClass);

        dataLoaders.put(loaderClass, dataLoader);
    }

    private static void registerViews() {
        for (View view : ClassGraphUtils.getExtendedClasses(View.class, "me.webhead1104.towncraft.features")) {
            viewFrame.with(view);
        }

        viewFrame.register();
    }
}
