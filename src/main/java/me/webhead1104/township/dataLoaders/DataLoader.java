package me.webhead1104.township.dataLoaders;

import com.google.common.base.Stopwatch;
import me.webhead1104.township.Township;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public interface DataLoader {
    void load();

    default ConfigurationNode getNodeFromFile(String path) {
        try (InputStream stream = getClass().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalStateException("Could not find resource file: " + path);
            }
            return Township.GSON_CONFIGURATION_LOADER.source(() -> new BufferedReader(new InputStreamReader(stream))).build().load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default <T> List<T> getListFromFile(String path, Class<T> clazz) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ConfigurationNode node = getNodeFromFile(path);
        if (!node.isList()) {
            throw new RuntimeException("Node is not list!");
        }
        try {
            List<T> result = node.getList(clazz);
            if (result.isEmpty()) {
                throw new RuntimeException("List is empty for path `" + path + "`!");
            }
            Township.logger.debug("Took {} to load list from path `{}`", stopwatch.stop().elapsed().toMillis(), path);
            return result;
        } catch (SerializationException e) {
            throw new RuntimeException("An error occurred whilst getting a list from file `" + path + "`!", e);
        }
    }
}
