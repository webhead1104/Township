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
