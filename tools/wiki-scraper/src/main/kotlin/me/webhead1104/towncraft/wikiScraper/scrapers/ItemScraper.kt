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
import io.github.oshai.kotlinlogging.KotlinLogging
import lombok.Getter
import me.webhead1104.towncraft.wikiScraper.Main
import me.webhead1104.towncraft.wikiScraper.data.Item
import me.webhead1104.towncraft.wikiScraper.utils.Page
import me.webhead1104.towncraft.wikiScraper.utils.Row
import me.webhead1104.towncraft.wikiScraper.utils.Utils
import org.jsoup.nodes.Element
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.util.function.Predicate
import java.util.function.UnaryOperator
import java.util.regex.Pattern

@Keep
class ItemScraper : Scraper<Item> {
    override fun id(): String {
        return "items"
    }

    override fun scrape(): List<Item> {
        val items: MutableList<Item> = ArrayList()
        items.add(Item("none", "minecraft:barrier", -1, -1))

        val tables = Page(fetchPage(BASE_URL), "div.mw-collapsible").tables

        for (table in tables) {
            val type: Type = Type.match(table.firstRow.text)
            if (type === Type.UNKNOWN) {
                logger.warn { "Skipping '${table.firstRow.text}' as it doesn't match anything" }
                continue
            }
            items.addAll(type.parse(table.rows))
        }
        items.removeIf { item -> item.levelNeeded > Main.MAX_LEVEL }
        return items
    }

    override fun resultType(): Class<Item> {
        return Item::class.java
    }

    @Getter
    internal enum class Type(
        pattern: String,
        private val startingRow: Int,
        private val keyCell: Int,
        private val levelCell: Int,
        private val coinCell: Int,
        private val neededSize: Int,
        private val noRun: Predicate<Int>,
        private val runAfterAll: UnaryOperator<MutableList<Item>> = UnaryOperator { items: MutableList<Item> -> items }
    ) {
        UNKNOWN("", -1, -1, -1, -1, -1, Predicate { _: Int -> true }),
        FARM("Click on link to go down to Feed Mill", 4, 0, 3, 4, 9, Predicate { i: Int -> i in 11..<15 }) {
            private var itemName: String? = null

            override fun parse(rows: MutableList<Row>): MutableList<Item> {
                var rows = rows
                rows = rows.subList(4, rows.size)
                val items: MutableList<Item> = ArrayList()
                for (i in rows.indices) {
                    val row = rows[i]
                    if (i == 12) {
                        itemName = row.text
                        continue
                    }
                    if (i in 11..<15) {
                        continue
                    }

                    if (row.values.size != 9) {
                        continue
                    }

                    val key = row.getValue(0).key
                    val material: String = ITEM_DATA.materials.getOrDefault(key, "minecraft:player_head")
                    val level = row.getValue(3).level
                    val coins = row.getValue(4).coins
                    if (i > 14) {
                        val xpGiven = row.getValue(5).xp
                        FEED_MILL_ITEMS.add(
                            Item(
                                key = key,
                                material = material,
                                levelNeeded = level,
                                sellPrice = coins,
                                factoryData = Item.FactoryData(
                                    factoryName = itemName!!,
                                    itemName = row.getValue(0).text,
                                    ingredients = parseIngredients(row),
                                    xpGiven = xpGiven,
                                    time = row.getValue(6).text
                                )
                            )
                        )
                        continue
                    }
                    items.add(
                        Item(
                            key = key,
                            material = material,
                            levelNeeded = level,
                            sellPrice = coins
                        )
                    )
                }
                return items.subList(0, 11)
            }
        },
        CROPS("Crops", 2, 0, 2, 5, 9, Predicate { i: Int -> i > 24 }),
        FACTORY(
            "Asian ~ .*",
            3,
            0,
            3,
            4,
            9,
            Predicate { i: Int -> i > 373 },
            UnaryOperator { items: MutableList<Item> ->
                items.addAll(0, FEED_MILL_ITEMS)
                items
            }) {
            private var lastName: String? = null

            override fun parse(rows: MutableList<Row>): MutableList<Item> {
                var rows = rows
                rows = rows.subList(1, rows.size)
                val items: MutableList<Item> = ArrayList(FEED_MILL_ITEMS)
                for (i in rows.indices) {
                    val row = rows[i]
                    if (i > 373) {
                        continue
                    }

                    if (row.values.size == 1) {
                        lastName = row.text
                        continue
                    }

                    if (row.values.size != 9) {
                        continue
                    }

                    val key = row.getValue(0).key
                    val material: String = ITEM_DATA.materials.getOrDefault(key, "minecraft:player_head")
                    val level = row.getValue(3).level
                    val coins = row.getValue(4).coins
                    val xpGiven = row.getValue(5).xp
                    items.add(
                        Item(
                            key = key,
                            material = material,
                            levelNeeded = level,
                            sellPrice = coins,
                            factoryData = Item.FactoryData(
                                factoryName = lastName!!,
                                itemName = row.getValue(0).text,
                                ingredients = parseIngredients(row),
                                xpGiven = xpGiven,
                                time = row.getValue(6).text
                            )
                        )
                    )
                }

                return items
            }
        },
        ISLAND("Islands and Ships", -1, -1, -1, -1, -1, Predicate { _: Int -> false }) {
            private var lastLevel = 0

            override fun parse(rows: MutableList<Row>): MutableList<Item> {
                var rows = rows
                rows = rows.subList(3, rows.size)
                val items: MutableList<Item> = ArrayList()
                for (i in rows.indices) {
                    val row = rows[i]
                    if (i > 18) {
                        return items
                    }

                    if (row.values.size != 8 && row.values.size != 6) {
                        continue
                    }

                    val key = row.getValue(0).key
                    if (row.values.size == 8) {
                        lastLevel = row.getValue(2).level
                    }
                    items.add(
                        Item(
                            key,
                            ITEM_DATA.materials.getOrDefault(key, "minecraft:player_head"),
                            lastLevel,
                            row.getValue(if (row.values.size == 8) 3 else 2).coins
                        )
                    )
                }
                return items
            }
        },
        MINE("Mine", 2, 0, 2, 3, 7, Predicate { _: Int -> false }) {
            override fun parse(rows: MutableList<Row>): MutableList<Item> {
                var rows = rows
                rows = rows.subList(2, rows.size)
                val items: MutableList<Item> = ArrayList()
                for (i in rows.indices) {
                    val row = rows[i]

                    if (row.values.size != 7) {
                        continue
                    }

                    val key = row.getValue(0).key
                    items.add(
                        Item(
                            key,
                            ITEM_DATA.materials.getOrDefault(key, "minecraft:player_head"),
                            row.getValue(2).level,
                            row.getValue(if (i > 10) 4 else 3).coins
                        )
                    )
                }
                return items
            }
        },
        CONSTRUCTION("Building Materials Barn Tools", -1, -1, -1, -1, -1, Predicate { _: Int? -> false }) {
            override fun parse(rows: MutableList<Row>): MutableList<Item> {
                var rows = rows
                rows = rows.subList(0, rows.size)
                val items: MutableList<Item> = ArrayList()
                val otherItems: MutableList<Item> = ArrayList()
                for (i in rows.indices) {
                    if (otherItems.size == 3) {
                        items.addAll(otherItems)
                        otherItems.clear()
                    }
                    val row = rows[i]
                    if (i >= 10) {
                        return items
                    }

                    if (row.values.size != 7) {
                        continue
                    }

                    items.add(
                        Item(
                            row.getValue(0).key,
                            ITEM_DATA.materials.getOrDefault(row.getValue(0).key, "minecraft:player_head"),
                            0,
                            row.getValue(2).coins,
                            8 // Very rare (special construction items)
                        )
                    )

                    otherItems.add(
                        Item(
                            row.getValue(4).key,
                            ITEM_DATA.materials.getOrDefault(row.getValue(4).key, "minecraft:player_head"),
                            0,
                            row.getValue(6).coins,
                            8 // Very rare (special construction items)
                        )
                    )
                }
                return items
            }
        };

        private val pattern: Pattern = Pattern.compile(pattern)

        fun parseIngredients(row: Row): MutableMap<String, Int> {
            val ingredients: MutableMap<String, Int> = LinkedHashMap()
            val html =
                row.element.select("th, td").stream().map<String> { obj: Element -> obj.wholeText() }.toList()[2]
            for (ingredient in html.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                var ingredient = ingredient
                ingredient = ingredient.trim { it <= ' ' }
                if (ingredient.isEmpty()) continue  // Skip empty lines

                val values = ingredient.split(" ".toRegex(), limit = 2).toTypedArray() // Split into at most 2 parts

                if (values.size == 2) {
                    val quantityStr = values[0]
                    val name = values[1]
                    ingredients[Utils.normalizeForKey(name)] = quantityStr.toInt()
                } else {
                    logger.warn { "Could not parse ingredient: $ingredient" }
                }
            }
            return ingredients
        }

        open fun parse(rows: MutableList<Row>): MutableList<Item> {
            var rows = rows
            rows = rows.subList(startingRow, rows.size)
            val items: MutableList<Item> = ArrayList()
            for (i in rows.indices) {
                val row = rows[i]
                if (noRun.test(i)) {
                    continue
                }

                if (row.values.size != neededSize) {
                    continue
                }


                val key = row.getValue(keyCell).key
                items.add(
                    Item(
                        key,
                        ITEM_DATA.materials.getOrDefault(key, "minecraft:player_head"),
                        row.getValue(levelCell).level,
                        row.getValue(coinCell).coins
                    )
                )
            }

            return runAfterAll.apply(items)
        }

        companion object {
            fun match(pattern: String): Type {
                for (type in entries) {
                    if (type.pattern.matcher(pattern).matches()) {
                        return type
                    }
                }

                return UNKNOWN
            }
        }
    }

    @ConfigSerializable
    data class ItemJson(val materials: Map<String, String> = emptyMap())

    companion object {
        val ITEM_DATA: ItemJson = Utils.readJson("/data/item_data.json", ItemJson::class.java)
        private const val BASE_URL = "https://township.fandom.com/wiki/Goods"
        private val FEED_MILL_ITEMS: MutableList<Item> = ArrayList()
        val logger = KotlinLogging.logger {}
    }
}
