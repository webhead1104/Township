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
package me.webhead1104.towncraft.wikiScraper.scrapers

import com.google.errorprone.annotations.Keep
import me.webhead1104.towncraft.wikiScraper.Main
import me.webhead1104.towncraft.wikiScraper.scrapers.LevelScraper.Level
import me.webhead1104.towncraft.wikiScraper.utils.Page
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@Keep
class LevelScraper : Scraper<Level> {
    override fun id(): String {
        return "levels"
    }

    override fun scrape(): MutableList<Level> {
        val levels: MutableList<Level> = ArrayList()
        val table = Page(fetchPage(BASE_URL), "table.article-table").tables[0]
        val rows = table.getRowsBetween(1, table.rows.size)
        for (i in 0..<rows.size) {
            val row = rows[i]
            if (row.values[0].level > Main.MAX_LEVEL) {
                break
            }
            if (row.values.size != 11) {
                continue
            }

            val xpText = row.element.select("th, td")[1].text().trim()
            val xpNeeded = if (xpText.isEmpty() || xpText == "-") {
                0
            } else {
                val cleanedXp = xpText.replace(",", "")
                cleanedXp.replace("\\D+".toRegex(), " ").trim().split(" ")[0].toIntOrNull() ?: 0
            }

            val rewardsCell = row.element.select("th, td")[2]
            var coinsGiven = 0
            var cashGiven = 0

            val numbers = rewardsCell.text().replace(",", "").split("\\s+".toRegex())
                .mapNotNull { it.toIntOrNull() }

            // Determine which rewards based on level (i)
            when {
                i <= 7 -> {
                    // Levels 1-7: coins and cash
                    if (numbers.isNotEmpty()) coinsGiven = numbers[0]
                    if (numbers.size > 1) cashGiven = numbers[1]
                }

                i < 66 -> {
                    // Levels 8-65: just coins
                    if (numbers.isNotEmpty()) coinsGiven = numbers[0]
                }

                else -> {
                    // Levels 66+: just cash
                    if (numbers.isNotEmpty()) cashGiven = numbers[0]
                }
            }

            levels.add(Level(xpNeeded, coinsGiven, cashGiven))
        }
        return levels
    }


    override fun resultType(): Class<Level> {
        return Level::class.java
    }

    @ConfigSerializable
    data class Level(
        @Setting("xp_needed")
        val xpNeeded: Int,
        @Setting("coins_given")
        val coinsGiven: Int,
        @Setting("cash_given")
        val cashGiven: Int
    )

    companion object {
        private const val BASE_URL = "https://township.fandom.com/wiki/Level_up"
    }
}
