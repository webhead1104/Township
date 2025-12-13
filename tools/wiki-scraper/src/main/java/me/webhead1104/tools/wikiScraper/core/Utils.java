package me.webhead1104.tools.wikiScraper.core;

import io.leangen.geantyref.TypeToken;
import lombok.experimental.UtilityClass;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.*;
import java.util.List;

@UtilityClass
public final class Utils {
    public static final GsonConfigurationLoader.Builder LOADER = GsonConfigurationLoader.builder();

    public static <T> File saveJson(List<T> data, File outDir, String fileName) throws IOException, IllegalArgumentException {
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IOException("Failed to create output directory: " + outDir);
        }

        if (data.isEmpty()) {
            throw new IllegalArgumentException("Cannot determine element type from empty list");
        }

        File file = new File(outDir, fileName);

        @SuppressWarnings("unchecked")
        Class<T> elementType = (Class<T>) data.getFirst().getClass();
        LOADER.file(file).build().save(BasicConfigurationNode.root().setList(elementType, data));
        return file;
    }

    public static <T> T readJson(String path, TypeToken<T> elementType) throws IOException, IllegalArgumentException {
        InputStream stream = Utils.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IOException("File Does not exist");
        }

        LOADER.source(() -> new BufferedReader(new InputStreamReader(stream)));
        T data = LOADER.build().load().get(elementType);
        stream.close();
        return data;
    }
}
