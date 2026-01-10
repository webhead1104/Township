/*
 * MIT License
 *
 * Copyright (c) 2026 Webhead1104
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
package me.webhead1104.towncraft.wikiScraper.data

import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@ConfigSerializable
data class BuildMenu(
    val key: String,
    var buildings: MutableList<String>? = null,
    @Transient
    val actualBuildings: MutableList<Building?>
) {
    init {
        this.buildings = ArrayList(actualBuildings.stream().map { obj -> obj!!.key }.distinct().toList())
    }
}

@ConfigSerializable
data class ConstructionMaterials(
    @Transient
    val string: String,
    var glass: Int = 0,
    var brick: Int = 0,
    var slab: Int = 0
) {
    init {
        val split: Array<String?> = string.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        this.glass = split[0]!!.toInt()
        this.brick = split[1]!!.toInt()
        this.slab = split[2]!!.toInt()
    }
}

@ConfigSerializable
data class Building(
    val key: String,
    @Setting("level_needed")
    var levelNeeded: Int = 0,
    @Transient
    var levelString: String? = null,
    @Setting("population_needed")
    var populationNeeded: Int? = null,
    @Setting("population_increase")
    var populationIncrease: Int? = null,
    @Setting("max_population_increase")
    var maxPopulationIncrease: Int? = null,
    var price: Price? = null,
    @Transient
    var priceString: String? = null,
    @Setting("construction_materials")
    var constructionMaterials: ConstructionMaterials? = null,
    var time: String? = null,
    var tile: Tile? = null,
    var xp: Int? = null,
    var size: TileSize? = null,
    @Setting("not_in_menu")
    var notInMenu: Boolean? = null
) {
    override fun equals(other: Any?): Boolean {
        if (other is Building) {
            return key == other.key
        }
        return false
    }

    override fun hashCode(): Int {
        var result = levelNeeded
        result = 31 * result + (populationNeeded ?: 0)
        result = 31 * result + (populationIncrease ?: 0)
        result = 31 * result + (maxPopulationIncrease ?: 0)
        result = 31 * result + (xp ?: 0)
        result = 31 * result + (notInMenu?.hashCode() ?: 0)
        result = 31 * result + key.hashCode()
        result = 31 * result + (levelString?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (priceString?.hashCode() ?: 0)
        result = 31 * result + (constructionMaterials?.hashCode() ?: 0)
        result = 31 * result + (time?.hashCode() ?: 0)
        result = 31 * result + (tile?.hashCode() ?: 0)
        result = 31 * result + (size?.hashCode() ?: 0)
        return result
    }
}
