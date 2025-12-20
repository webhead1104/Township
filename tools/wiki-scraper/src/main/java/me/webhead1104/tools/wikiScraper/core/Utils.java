package me.webhead1104.tools.wikiScraper.core;

import io.leangen.geantyref.TypeToken;
import lombok.experimental.UtilityClass;
import me.webhead1104.tools.wikiScraper.model.tile.Tile;
import me.webhead1104.tools.wikiScraper.model.tile.TileSize;
import me.webhead1104.tools.wikiScraper.serializers.TileSerializer;
import me.webhead1104.tools.wikiScraper.serializers.TileSizeSerializer;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.io.*;
import java.text.Normalizer;
import java.util.List;

@UtilityClass
public final class Utils {
    public static final GsonConfigurationLoader.Builder LOADER = GsonConfigurationLoader.builder()
            .defaultOptions(opts -> opts.shouldCopyDefaults(true).serializers(builder -> {
                builder.register(Tile.class, new TileSerializer());
                builder.register(TileSize.class, new TileSizeSerializer());
            }));

    public static <T> File saveJson(List<T> data, File outDir, String fileName, Class<T> elementType) throws IOException, IllegalArgumentException {
        if (!outDir.exists() && !outDir.mkdirs()) {
            throw new IOException("Failed to create output directory: " + outDir);
        }
        File file = new File(outDir, fileName);
        LOADER.file(file).build().save(LOADER.build().createNode().setList(elementType, data));
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

    public static String normalizeForKey(String itemName) {
        String normalized = itemName.toLowerCase();

        // Remove accents/diacritics (é -> e, ê -> e, etc.)
        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", ""); // Remove diacritical marks

        normalized = normalized.replaceAll(" ", "_");
        normalized = normalized.replaceAll("-", "_");
        normalized = normalized.replaceAll("'", "");
        normalized = normalized.replaceAll("_x3", "");

        normalized = fix(normalized);
        return normalized;
    }

    private String fix(String string) {
        return switch (string) {
            case "carrots" -> "carrot";
            case "cookie" -> "cookies";
            case "strawberries" -> "strawberry";
            case "honeycomb", "honey" -> "honeycombs";
            case "grape" -> "grapes";
            case "olive" -> "olives";
            case "eggs" -> "egg";
            case "coconuts" -> "coconut";
            case "colorful_feathers" -> "colorful_feather";
            case "roses" -> "rose";
            case "mushrooms" -> "mushroom";
            case "peanut_plants" -> "peanut_plant";
            case "corn_chip" -> "corn_chips";
            case "tea_plants" -> "tea_plant";
            case "tea_bag" -> "tea_bags";
            case "bronze_ores", "bronze_ore" -> "copper_ore";
            case "silver_ores" -> "silver_ore";
            case "gold_ores" -> "gold_ore";
            case "platinum_ores" -> "platinum_ore";
            default -> string;
        };
    }
}
