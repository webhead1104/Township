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
package me.webhead1104.towncraft.wikiScraper.scrapers

import com.google.errorprone.annotations.Keep
import me.webhead1104.towncraft.wikiScraper.Main
import me.webhead1104.towncraft.wikiScraper.annotations.DependsOn
import me.webhead1104.towncraft.wikiScraper.data.*
import me.webhead1104.towncraft.wikiScraper.utils.Utils
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File
import java.util.function.Function
import java.util.regex.Pattern
import java.util.stream.Collectors

@Keep
@DependsOn(BuildMenuScraper::class)
class BuildingTypeScraper : Scraper<BuildingTypeScraper.BuildingType> {
    override fun id(): String {
        return "buildingTypes"
    }

    override fun scrape(): MutableList<BuildingType> {
        val results: MutableList<BuildingType> = ArrayList()
        val buildMenus: MutableList<BuildMenu> = getFrom(BuildMenuScraper::class.java)
        for (buildMenu in buildMenus) {
            val buildings: MutableList<Building?> = ArrayList()
            for (actualBuilding in buildMenu.actualBuildings) {
                if (buildMenu.key == "special") {
                    actualBuilding!!.tile = SPECIAL_BUILDING_TILES[actualBuilding.key]
                    buildings.add(actualBuilding)
                    actualBuilding.size =
                        SPECIAL_BUILDING_TILESIZE.getOrDefault(actualBuilding.key, actualBuilding.size)
                    if (NOT_IN_MENU.contains(actualBuilding.key)) {
                        actualBuilding.notInMenu = true
                    }
                } else if (buildMenu.key == "housing") {
                    val regex = """(\d+) @ lvl (\d+)""".toRegex()
                    var houseNumber = 1
                    regex.findAll(actualBuilding!!.levelString!!).forEach { matchResult ->
                        val level = matchResult.groupValues[2].toInt()
                        if (level > Main.MAX_LEVEL) {
                            houseNumber++
                            return@forEach
                        }

                        val isFree = isFreeHouse(actualBuilding.priceString!!, houseNumber)

                        val price: Price?
                        var time: String?
                        val xp: Int

                        if (isFree) {
                            price = Price()
                            time = ""
                            xp = 0
                        } else {
                            price = actualBuilding.price
                            time = actualBuilding.time
                                ?.replace("\\(except .*\\)".toRegex(), "")?.trim()
                                ?.replace("[Nn]/[Aa]".toRegex(), "")?.trim()
                                ?.replace("Instant".toRegex(), "")?.trim()
                            xp = actualBuilding.xp ?: 0
                        }

                        buildings.add(
                            actualBuilding.copy(
                                levelNeeded = level,
                                price = price,
                                time = time,
                                xp = xp
                            )
                        )
                        houseNumber++
                    }
                } else {
                    if (actualBuilding!!.time != null) {
                        actualBuilding.time = actualBuilding.time!!.replace("[Nn]/[Aa]".toRegex(), "")
                        actualBuilding.time = actualBuilding.time!!.replace("\\(except .*\\)".toRegex(), "")
                        actualBuilding.time = actualBuilding.time!!.replace("Instant".toRegex(), "")
                    }
                    buildings.add(actualBuilding)
                }
            }
            results.add(BuildingType(buildMenu.key, buildings, buildMenu.key))
        }
        return results
    }

    private fun isFreeHouse(priceString: String, houseNumber: Int): Boolean {
        val pattern = Pattern.compile("\\(free for #([\\d,]+)\\)")
        val matcher = pattern.matcher(priceString)

        if (matcher.find()) {
            val numbersStr = matcher.group(1)
            val numbers = numbersStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            for (num in numbers) {
                if (num.trim { it <= ' ' }.toInt() == houseNumber) {
                    return true
                }
            }
        }
        return false
    }

    override fun save(data: List<BuildingType>, outDir: File): File {
        val groupedByType = data.stream()
            .collect(Collectors.groupingBy(Function { obj: BuildingType -> obj.type }))


        for (entry in groupedByType.entries) {
            val type = entry.key
            val typeGroup: MutableList<BuildingType> = entry.value

            Utils.saveJson(
                typeGroup,
                File(outDir, "buildingTypes"),
                "$type.json",
                BuildingType::class.java
            )
        }
        return File(outDir, "buildingTypes")
    }

    override fun resultType(): Class<BuildingType> {
        return BuildingType::class.java
    }

    @ConfigSerializable
    data class BuildingType(
        val key: String,
        val buildings: List<Building?>,
        @Transient
        val type: String
    )

    companion object {
        val SPECIAL_BUILDING_TILES: MutableMap<String, Tile> = HashMap()
        val SPECIAL_BUILDING_TILESIZE: MutableMap<String, TileSize> = HashMap()
        val NOT_IN_MENU: MutableList<String?> = ArrayList()

        init {
            SPECIAL_BUILDING_TILES["barn"] = BarnTile
            SPECIAL_BUILDING_TILES["helicopter"] = HelicopterTile
            SPECIAL_BUILDING_TILES["town_hall"] = TOWN_HALL_TILE
            SPECIAL_BUILDING_TILES["event_center"] = EventCenterTile
            SPECIAL_BUILDING_TILESIZE["event_center"] = TileSize(3, 5)
            NOT_IN_MENU.add("event_center")
            SPECIAL_BUILDING_TILES["train"] = TrainTile
            SPECIAL_BUILDING_TILESIZE["train"] = TileSize(1, 3)
            NOT_IN_MENU.add("train")
        }
    }
}
