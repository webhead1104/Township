package me.webhead1104.tools.wikiScraper.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@UtilityClass
public final class Outputs {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static File saveJson(List<?> data, File outDir, String filename) throws IOException {
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IOException("Failed to create output directory: " + outDir);
        }
        File out = new File(outDir, filename);
        try (BufferedWriter writer = Files.newBufferedWriter(out.toPath(), StandardCharsets.UTF_8)) {
            GSON.toJson(data, writer);
        }
        return out;
    }
}
