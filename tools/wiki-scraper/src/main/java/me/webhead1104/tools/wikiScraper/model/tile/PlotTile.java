package me.webhead1104.tools.wikiScraper.model.tile;

import lombok.Getter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@Getter
public class PlotTile extends Tile {
    private final String plotType = "none";
}
