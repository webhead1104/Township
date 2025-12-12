package me.webhead1104.towncraft;

import me.devnatan.inventoryframework.ViewFrame;
import me.webhead1104.towncraft.dataLoaders.DataLoader;
import me.webhead1104.towncraft.database.UserLoader;
import me.webhead1104.towncraft.serializers.TowncraftSerializer;
import me.webhead1104.towncraft.utils.ClassGraphUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.util.Services;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.File;
import java.util.UUID;

public interface Towncraft {
    TowncraftPlatform PLATFORM = Services.service(TowncraftPlatform.class)
            .orElseThrow(() -> new IllegalStateException("No Towncraft found!"));
    @SuppressWarnings("unchecked")
    GsonConfigurationLoader.Builder GSON_CONFIGURATION_LOADER = GsonConfigurationLoader.builder().defaultOptions(opts -> opts.shouldCopyDefaults(true).serializers(builder -> {
        for (TowncraftSerializer<?> towncraftSerializer : ClassGraphUtils.getExtendedClasses(TowncraftSerializer.class, "me.webhead1104.towncraft.serializers")) {
            TowncraftSerializer<Object> serializer = (TowncraftSerializer<Object>) towncraftSerializer;
            builder.register(serializer.getType(), serializer);
        }
    }));
    Key NONE_KEY = key("none");

    static Key key(@KeyPattern.Value String value) {
        return Key.key("towncraft", value);
    }

    static <T extends DataLoader> T getDataLoader(Class<T> dataLoaderClass) {
        return TowncraftPlatformManager.getDataLoader(dataLoaderClass);
    }

    static ViewFrame getViewFrame() {
        return TowncraftPlatformManager.getViewFrame();
    }

    @Nullable
    static TowncraftPlayer getPlayer(UUID uuid) {
        return PLATFORM.getPlayer(uuid);
    }

    static Logger getLogger() {
        return PLATFORM.getLogger();
    }

    static Config getConfig() {
        return TowncraftPlatformManager.getConfig();
    }

    static File getDataFolder() {
        return PLATFORM.getDataFolder();
    }

    static UserLoader getUserLoader() {
        return TowncraftPlatformManager.getLoaderManager().getUserLoader();
    }
}
