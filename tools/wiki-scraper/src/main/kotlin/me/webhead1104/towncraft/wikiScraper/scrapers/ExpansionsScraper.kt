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
import me.webhead1104.towncraft.wikiScraper.utils.Page
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting

@Keep
class ExpansionsScraper : Scraper<ExpansionsScraper.Expansion> {
    override fun id(): String {
        return "expansions"
    }

    override fun scrape(): MutableList<Expansion> {
        val expansions: MutableList<Expansion> = ArrayList()
        val page = Page(fetchPage(BASE_URL), "table.article-table")

        for (table in page.tables) {
            for (i in table.rows.indices) {
                if (i == 0 || i == 1) {
                    continue
                }
                if (expansions.size >= MAX_EXPANSIONS) {
                    break
                }

                val row = table.rows[i]
                val populationNeeded = row.getValue(1).population
                val coinsNeeded = row.getValue(3).coins
                val time = row.getValue(4).text
                val xpNeeded = row.getValue(5).xp
                val elements = row.getValue(2).text.split(" ")
                val toolsNeeded = Tools(
                    axe = elements[0].toInt(),
                    shovel = elements[1].toInt(),
                    saw = elements[2].toInt()
                )
                expansions.add(
                    Expansion(
                        populationNeeded = populationNeeded,
                        coinsNeeded = coinsNeeded,
                        time = time,
                        xpNeeded = xpNeeded,
                        toolsNeeded = toolsNeeded
                    )
                )
            }
        }
        return expansions
    }

    override fun resultType(): Class<Expansion> {
        return Expansion::class.java
    }

    @ConfigSerializable
    data class Expansion(
        @Setting("population_needed")
        val populationNeeded: Int,
        @Setting("coins_needed")
        val coinsNeeded: Int,
        val time: String,
        @Setting("xp_needed")
        val xpNeeded: Int,
        @Setting("tools_needed")
        val toolsNeeded: Tools
    )

    @ConfigSerializable
    data class Tools(
        val axe: Int,
        val shovel: Int,
        val saw: Int
    )

    companion object {
        const val MAX_EXPANSIONS: Int = 357
        private const val BASE_URL = "https://township.fandom.com/wiki/Expansions"
    }
}
