package me.webhead1104.tools.wikiScraper.model.tile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class StaticWorldTile extends Tile {
    public static final StaticWorldTile TOWN_HALL = new StaticWorldTile("minecraft:cobblestone");
    private String material;
}
