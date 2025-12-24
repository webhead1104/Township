/*
 * MIT License
 *
 * Copyright (c) 2025 Webhead1104
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
