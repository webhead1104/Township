package me.webhead1104.towncraft;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewFrame;
import me.webhead1104.towncraft.annotations.DependsOn;
import me.webhead1104.towncraft.commands.arguments.AnimalTypeArgument;
import me.webhead1104.towncraft.commands.arguments.FactoryTypeArgument;
import me.webhead1104.towncraft.commands.arguments.ItemTypeArgument;
import me.webhead1104.towncraft.commands.arguments.TowncraftPlayerArgument;
import me.webhead1104.towncraft.commands.subCommands.TowncraftSubCommand;
import me.webhead1104.towncraft.data.objects.User;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.dataLoaders.ItemType;
import me.webhead1104.towncraft.dataVersions.DataVersion;
import me.webhead1104.towncraft.database.LoaderManager;
import me.webhead1104.towncraft.database.UserLoader;
import me.webhead1104.towncraft.features.animals.AnimalType;
import me.webhead1104.towncraft.features.factories.FactoryType;
import me.webhead1104.towncraft.utils.ClassGraphUtils;
import net.kyori.adventure.util.Services;
import org.spongepowered.configurate.transformation.ConfigurationTransformation;
import revxrsal.commands.Lamp;
import revxrsal.commands.command.CommandActor;
import revxrsal.commands.orphan.Orphans;
import revxrsal.commands.parameter.ParameterTypes;

import java.io.IOException;
import java.util.*;

public class TowncraftPlatformManager {
    @Getter
    private final static TowncraftPlatform platform = Services.service(TowncraftPlatform.class)
            .orElseThrow(() -> new IllegalStateException("No Towncraft found!"));
    @Getter
    private static final ConfigurationTransformation.VersionedBuilder versionedBuilder = ConfigurationTransformation.versionedBuilder();
    @Getter
    private static final Map<Class<? extends DataLoader>, DataLoader> dataLoaders = new HashMap<>();
    @Getter
    private static ViewFrame viewFrame;
    @Getter
    private static UserManager userManager;
    @Getter
    @Setter
    private static LoaderManager loaderManager;
    @Getter
    private static InventoryManager inventoryManager;
    @Getter
    @Setter
    private static Config config;

    public static <T extends DataLoader> T getDataLoader(Class<T> dataLoaderClass) {
        T loader = dataLoaderClass.cast(dataLoaders.get(dataLoaderClass));
        if (loader == null) {
            throw new IllegalStateException("Data loader not found: " + dataLoaderClass.getName());
        }
        return loader;
    }

    public static void init() {
        Config.loadConfig();
        loadDataLoaders();
        loadDataVersions();
        viewFrame = new ViewFrame();
        registerViews();

        userManager = new UserManager();
        inventoryManager = new InventoryManager();
    }


    public static <A extends CommandActor> void registerParameterTypes(ParameterTypes.Builder<A> builder) {
        builder.addParameterType(AnimalType.Animal.class, new AnimalTypeArgument<>());
        builder.addParameterType(FactoryType.Factory.class, new FactoryTypeArgument<>());
        builder.addParameterType(ItemType.Item.class, new ItemTypeArgument<>());
        builder.addContextParameter(TowncraftPlayer.class, new TowncraftPlayerArgument<>());
    }

    public static <A extends CommandActor> void initCommands(Lamp<A> lamp) {
        for (TowncraftSubCommand implementedClass : ClassGraphUtils.getImplementedClasses(TowncraftSubCommand.class, "me.webhead1104.towncraft.commands.subCommands")) {
            lamp.register(Orphans.path("towncraft").handler(implementedClass));
        }
    }

    public static void onJoin(TowncraftPlayer player) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        UserLoader userLoader = Towncraft.getUserLoader();
        TowncraftPlatformManager.getPlatform().runTaskAsync(() -> {
            try {
                if (userLoader.userExists(player.getUUID())) {
                    User user = User.fromJson(userLoader.readUser(player.getUUID()));
                    TowncraftPlatformManager.getUserManager().setUser(player.getUUID(), user);
                    Towncraft.getLogger().info("Data has been loaded for {}", player.getName());
                    return;
                }
                Towncraft.getLogger().info("Created new user for {}", player.getName());
                User user = new User(player.getUUID());
                userLoader.saveUser(player.getUUID(), user.toString());
                TowncraftPlatformManager.getUserManager().setUser(player.getUUID(), user);
            } catch (IOException e) {
                Towncraft.getLogger().error("An error occurred whilst loading player data!", e);
            }
            Towncraft.getLogger().info("Join event done in {}ms", stopwatch.elapsed().toMillis());
        });
    }

    public static void onLeave(TowncraftPlayer player) {
        player.getUser().save();
        userManager.removeUser(player.getUUID());
        Towncraft.getLogger().info("Data has been saved for {}", player.getName());
    }

    public static void shutdown() {
        viewFrame.unregister();
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

    private static void loadDataVersions() {
        versionedBuilder.versionKey("version");
        for (DataVersion dataVersion : new ArrayList<>(ClassGraphUtils.getImplementedClasses(DataVersion.class, "me.webhead1104.towncraft.dataVersions"))) {
            versionedBuilder.addVersion(dataVersion.getVersion(), dataVersion.getTransformation());
        }
    }
}
