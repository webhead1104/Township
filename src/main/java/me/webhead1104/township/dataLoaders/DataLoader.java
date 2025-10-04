package me.webhead1104.township.dataLoaders;

import me.webhead1104.township.Township;
import org.spongepowered.configurate.ConfigurationNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
