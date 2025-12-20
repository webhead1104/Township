package me.webhead1104.tools.wikiScraper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import me.webhead1104.tools.wikiScraper.model.tile.Tile;
import me.webhead1104.tools.wikiScraper.model.tile.TileSize;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@ConfigSerializable
public class BuildMenu {
    private String key;
    private List<String> buildings;
    private transient List<Building> actualBuildings;

    public BuildMenu(String key, List<Building> actualBuildings) {
        this.key = key;
        this.actualBuildings = actualBuildings;
        this.buildings = new ArrayList<>(actualBuildings.stream().map(Building::getKey).distinct().toList());
    }

    @With
    @Getter
    @Setter
    @AllArgsConstructor
    @ConfigSerializable
    public static class Building {
        private final String key;
        @Setting("level_needed")
        private int levelNeeded;
        private transient String levelString;
        @Setting("population_needed")
        private Integer populationNeeded;
        @Setting("population_increase")
        private Integer populationIncrease;
        @Setting("max_population_increase")
        private Integer maxPopulationIncrease;
        private Price price;
        private transient String priceString;
        @Setting("construction_materials")
        private ConstructionMaterials constructionMaterials;
        private String time;
        private Tile tile;
        private Integer xp;
        @Setting("size")
        private TileSize size;
        private Boolean notInMenu;

        public Building(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Building building) {
                return key.equals(building.key);
            }

            return false;
        }
    }
}
