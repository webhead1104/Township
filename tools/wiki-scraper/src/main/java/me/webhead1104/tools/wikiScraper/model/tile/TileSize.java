package me.webhead1104.tools.wikiScraper.model.tile;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record TileSize(int height, int width) {

    public TileSize(String string) {
        this(parse(string, 0), parse(string, 1));
    }

    public static int parse(String string, int slot) {
        if (!string.matches(".*\\D.*")) {
            return 1;
        }
        if (string.equals("Fixed")) {
            return 0;
        }
        return Integer.parseInt(string.split(" x ")[slot]);
    }
}
