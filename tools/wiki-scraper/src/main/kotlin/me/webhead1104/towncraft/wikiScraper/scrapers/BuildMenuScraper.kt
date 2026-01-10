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
import lombok.Getter
import me.webhead1104.towncraft.wikiScraper.Main
import me.webhead1104.towncraft.wikiScraper.data.*
import me.webhead1104.towncraft.wikiScraper.utils.Page
import me.webhead1104.towncraft.wikiScraper.utils.Row
import me.webhead1104.towncraft.wikiScraper.utils.Table
import me.webhead1104.towncraft.wikiScraper.utils.Utils
import java.util.*
import java.util.function.Consumer

@Keep
class BuildMenuScraper : Scraper<BuildMenu> {
    override fun id(): String {
        return "buildMenus"
    }

    override fun scrape(): MutableList<BuildMenu> {
        val buildMenus: MutableMap<String, BuildMenu> = LinkedHashMap()

        for (buildMenuType in BuildMenuTypes.entries) {
            val page = Page(fetchPage(buildMenuType.page), buildMenuType.cssSelector)
            val table: Table = page.tables.first()
            val buildings: MutableList<Building?> = ArrayList()
            for (i in buildMenuType.startRow..<table.rows.size) {
                if (i == buildMenuType.endRow) {
                    break
                }
                val row = table.rows[i]

                buildings.add(buildMenuType.parseRow(row))
            }
            buildings.removeIf { obj: Building? -> Objects.isNull(obj) }
            buildings.removeIf { building: Building? ->
                building!!.levelNeeded > Main.MAX_LEVEL
            }
            if (buildMenuType === BuildMenuTypes.FARMING) {
                buildings.addAll(0, parsePlots())
            }

            val buildMenu = BuildMenu(buildMenuType.name.lowercase(Locale.getDefault()), actualBuildings = buildings)
            buildMenus[buildMenuType.name.lowercase(Locale.getDefault())] = buildMenu
        }
        return ArrayList(buildMenus.values)
    }

    override fun resultType(): Class<BuildMenu> {
        return BuildMenu::class.java
    }

    private fun parsePlots(): MutableList<Building?> {
        val buildings: MutableList<Building?> = ArrayList()
        repeat(6) {
            buildings.add(
                Building(
                    key = "plot",
                    tile = PlotTile(),
                    size = TileSize(1, 1)
                )
            )
        }
        var isFirstTable = true
        for (table in Page(fetchPage("https://township.fandom.com/wiki/Fields"), ".article-table").tables) {
            table.getRowsBetween(if (isFirstTable) 2 else 1, table.rows.size).forEach(Consumer { row: Row? ->
                val building = Building("plot")
                building.populationNeeded = row!!.getValue(1).population
                if (building.populationNeeded!! > Main.MAX_POPULATION) {
                    return@Consumer
                }
                building.xp = row.getValue(2).xp
                building.tile = PlotTile()
                building.size = TileSize(1, 1)
                buildings.add(building)
            })
            isFirstTable = false
        }
        return buildings
    }

    @Getter
    enum class BuildMenuTypes(var page: String, val cssSelector: String, val startRow: Int, val endRow: Int) {
        HOUSING("Houses", ".article-table", 1, -1) {
            private var lastTime: String? = null
            private var lastXp = 0
            private var lastTileSize: TileSize? = null

            override fun parseRow(row: Row): Building {
                if (row.values.size > 5) {
                    val timeText = row.getValue(5).text
                    if (timeText.matches(".*[DdHhMmSs].*".toRegex())) {
                        lastTime = timeText.replace("\\s*\\(except.*?\\)".toRegex(), "").replace("n/a".toRegex(), "")
                            .trim { it <= ' ' }
                    }
                }

                if (row.values.size > 6) {
                    val xpText = row.getValue(6).text
                    if (xpText.matches(".*\\d.*xp.*".toRegex())) {
                        val cleanXp = xpText.replace("\\s*\\(.*?\\)".toRegex(), "").trim { it <= ' ' }
                        val xpNumber = cleanXp.replace("xp.*".toRegex(), "").replace("[^0-9]".toRegex(), "")
                            .trim { it <= ' ' }
                        if (!xpNumber.isEmpty()) {
                            lastXp = xpNumber.toInt()
                        }
                    }
                }

                if (row.values.size > 7 && row.getValue(7).text.matches(".*\\d.*".toRegex())) {
                    lastTileSize = TileSize(row.getValue(7).text)
                }

                val building = Building(row.getValue(1).key)
                building.levelNeeded = row.getValue(2).text.split("@ lvl ".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[1].split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].toInt()
                building.levelString = row.getValue(2).text
                building.populationIncrease = row.getValue(3).population

                val priceText = row.getValue(4).text
                val cleanPriceText = priceText.replace("\\s*\\(free for.*?\\)".toRegex(), "").trim { it <= ' ' }
                val priceNumber = cleanPriceText.replace("\\D+".toRegex(), "")
                building.price = Price(amount = if (priceNumber.isEmpty()) 0 else priceNumber.toInt())
                building.priceString = priceText

                building.time = lastTime
                building.size = lastTileSize
                building.tile = HouseTile(row.getValue(1).key.uppercase(Locale.getDefault()))
                building.xp = lastXp

                return building
            }
        },
        COMMUNITY_BUILDINGS("Community_Buildings", ".sortable", 1, -1) {
            override fun parseRow(row: Row): Building? {
                if (row.getValue(1).level >= 60) {
                    //todo community buildings above level 60 use drills, saws, and jackhammers
                    return null
                }
                val building = Building(row.getValue(0).key)
                building.levelNeeded = row.getValue(1).level
                building.maxPopulationIncrease = row.getValue(2).population
                building.price = Price(amount = row.getValue(3).coins)
                building.constructionMaterials = ConstructionMaterials(row.getValue(4).text)
                building.time = row.getValue(5).text
                building.size = TileSize(row.getValue(7).text)
                building.tile = CommunityBuildingTile(row.getValue(0).key.uppercase(Locale.getDefault()))
                building.xp = row.getValue(6).xp
                return building
            }
        },
        FACTORIES("Factories", "table.article-table:nth-of-type(2)", 1, -1) {
            private var feedMillKey: String? = null
            private var feedMillCount = 2

            override fun parseRow(row: Row): Building {
                if (row.getValue(1).text.contains("x3")) {
                    feedMillKey = row.getValue(1).key
                }
                if (row.values.size == 6) {
                    val building = Building(feedMillKey!!)
                    building.levelNeeded = row.getValue(0).level
                    building.populationNeeded = row.getValue(1).population
                    building.price = Price(amount = row.getValue(2).coins)
                    building.time = row.getValue(3).text
                    building.xp = row.getValue(4).xp
                    building.tile = FactoryTile(feedMillKey!!.uppercase(Locale.getDefault()) + "_" + feedMillCount++)
                    return building
                }
                val building = Building(row.getValue(1).key)
                building.levelNeeded = row.getValue(2).level
                building.populationNeeded = row.getValue(3).population
                building.price = Price(amount = row.getValue(4).coins)
                building.time = row.getValue(5).text
                building.size = TileSize(2, 2)
                val factoryType = if (row.getValue(1).key == "feed_mill") "feed_mill_1" else row.getValue(1).key
                building.tile = FactoryTile(factoryType.uppercase(Locale.getDefault()))
                building.xp = row.getValue(6).xp
                return building
            }
        },
        FARMING("Farming", ".wikitable", 1, 16) {
            private var lastName: String? = null
            private var count = 1

            override fun parseRow(row: Row): Building? {
                if (row.values.size == 9) {
                    lastName = row.getValue(0).text
                    count = 1
                }
                if (lastName!!.contains("Duck Feeder") || lastName!!.contains("Otter Pond") || lastName!!.contains("MushroomFarm")) {
                    //todo add support for duck feeder, otter pond, and mushroom farm
                    return null
                }
                val building = Building(Utils.normalizeForKey(lastName!!))
                val offset = if (row.values.size == 9) 0 else -1
                building.levelNeeded = row.getValue(offset + 2).level
                building.price = Price(amount = row.getValue(offset + 3).coins)
                building.time = row.getValue(offset + 4).text.replace("-".toRegex(), "")
                building.size = TileSize(2, 2)
                building.tile = AnimalTile(Utils.normalizeForKey(lastName!!) + "_" + count)
                building.xp = row.getValue(offset + 5).xp
                count++
                return building
            }
        },
        SPECIAL("Special_Buildings", "table.article-table:nth-child(1)", 2, -1) {
            override fun parseRow(row: Row): Building? {
                if (row.values.size != 7) {
                    return null
                }
                val building = Building(row.getValue(0).key)
                if (row.text.matches("[23]:".toRegex())) {
                    return null
                }
                if (row.text.contains("Train")) {
                    building.levelNeeded = row.getValue(1).text.replace("Train \\d: ".toRegex(), "").toInt()
                } else {
                    building.levelNeeded = row.getValue(1).level
                }
                building.populationNeeded = row.getValue(2).population
                building.price = Price(amount = row.getValue(3).coins)
                building.time = row.getValue(4).text.replace("(n/a)|(Instant.*)|".toRegex(), "")
                building.xp = row.getValue(5).xp
                building.size = TileSize(row.getValue(6).text)
                return building
            }
        };

        init {
            this.page = "https://township.fandom.com/wiki/$page"
        }

        open fun parseRow(row: Row): Building? {
            return null
        }
    }
}
